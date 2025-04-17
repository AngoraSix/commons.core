package com.angorasix.commons.servlet.presentation.error

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Links
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.function.ServerResponse

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
fun resolveExceptionResponse(
    ex: Exception,
    element: String = "Element",
    links: Links? = null,
): ServerResponse =
    when (ex) {
        is IllegalArgumentException ->
            ServerResponse
                .badRequest()
                .contentType(MediaTypes.HAL_FORMS_JSON)
                .body(
                    resolveProblem(
                        HttpStatus.BAD_REQUEST,
                        ex.javaClass.simpleName,
                        element,
                        ex.message,
                        links,
                    ),
                )

        else ->
            ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaTypes.HAL_FORMS_JSON)
                .body(
                    resolveProblem(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.javaClass.simpleName,
                        element,
                        ex.message,
                        links,
                    ),
                )
    }

fun resolveNotFound(
    message: String = "Element not found",
    element: String = "Element",
    links: Links? = null,
): ServerResponse =
    ServerResponse
        .status(HttpStatus.NOT_FOUND)
        .contentType(MediaTypes.HAL_FORMS_JSON)
        .body(
            resolveProblem(
                HttpStatus.NOT_FOUND,
                "ElementNotFound",
                element,
                message,
                links,
            ),
        )

fun resolveBadRequest(
    message: String = "Element is invalid",
    element: String = "Element",
    links: Links? = null,
): ServerResponse =
    ServerResponse
        .status(HttpStatus.BAD_REQUEST)
        .contentType(MediaTypes.HAL_FORMS_JSON)
        .body(
            resolveProblem(HttpStatus.BAD_REQUEST, "ElementInvalid", element, message, links),
        )

fun resolveUnauthorized(
    message: String = "Element is invalid",
    element: String = "Element",
    links: Links? = null,
): ServerResponse =
    ServerResponse
        .status(HttpStatus.UNAUTHORIZED)
        .contentType(MediaTypes.HAL_FORMS_JSON)
        .body(
            resolveProblem(
                HttpStatus.UNAUTHORIZED,
                "UnauthorizedDueToElement",
                element,
                message,
                links,
            ),
        )

fun resolveProblem(
    status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    error: String? = "",
    element: String = "ELEMENT",
    message: String? = null,
    links: Links? = null,
): EntityModel<out Problem> {
    val errorCode: String? = resolveErrorCode(status, element)
    val message: String? = message ?: resolveErrorMessage(status, element)
    val problem =
        Problem
            .create()
            .withStatus(status)
            .withTitle(error)
            .withDetail(message)
            .withProperties { map ->
                map["errorCode"] = errorCode
            }
    val hateoasProblem = EntityModel.of(problem)
    hateoasProblem.addAllIf(links != null && !links.isEmpty) { links }
    return hateoasProblem
}

private fun resolveErrorCode(
    status: HttpStatus,
    element: String,
): String =
    when (status) {
        HttpStatus.BAD_REQUEST -> "${element.replace(" ", "_").uppercase()}_INVALID"
        HttpStatus.NOT_FOUND -> "${element.replace(" ", "_").uppercase()}_NOT_FOUND"
        HttpStatus.UNAUTHORIZED -> "${element.replace(" ", "_").uppercase()}_UNAUTHORIZED"
        else -> "${element.replace(" ", "_")?.uppercase()}_ERROR"
    }

private fun resolveErrorMessage(
    status: HttpStatus,
    element: String?,
): String =
    when (status) {
        HttpStatus.BAD_REQUEST -> "$element is invalid"
        HttpStatus.NOT_FOUND -> "$element not found"
        HttpStatus.UNAUTHORIZED -> "$element is not authorized"
        else -> "error with $element"
    }
