package com.angorasix.commons.reactive.presentation.mappings

import com.angorasix.commons.infrastructure.config.configurationproperty.api.Route
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.mediatype.Affordances
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.util.ForwardedHeaderUtils

@Suppress("SpreadOperator")
fun RepresentationModel<*>.addSelfLink(
    route: Route,
    request: ServerRequest,
    expandArgs: List<String> = emptyList(),
) {
    val actionLink =
        Link
            .of(
                uriBuilder(request)
                    .path(route.resolvePath())
                    .build()
                    .toUriString(),
            ).expand(expandArgs)
            .withSelfRel()
    val affordanceLink =
        Affordances
            .of(actionLink)
            .afford(HttpMethod.OPTIONS)
            .withName(IanaLinkRelations.SELF_VALUE)
            .toLink()
    add(affordanceLink)
}

@Suppress("SpreadOperator")
fun RepresentationModel<*>.addLink(
    route: Route,
    actionName: String,
    request: ServerRequest,
    expandArgs: List<String> = emptyList(),
    inputClazz: Class<*>? = null,
) {
    val actionLink =
        Link
            .of(
                uriBuilder(request)
                    .path(route.resolvePath())
                    .build()
                    .toUriString(),
            ).expand(*expandArgs.toTypedArray())
            .withTitle(actionName)
            .withName(actionName)
            .withRel(actionName)
    val affordanceLink =
        Affordances
            .of(actionLink)
            .afford(route.method)
            .withName(actionName)
            .let { builder ->
                // If inputClazz is not null, invoke withInput and return the updated builder
                inputClazz?.let { builder.withInput(it) } ?: builder
            }.toLink()
    add(affordanceLink)
}

private fun uriBuilder(request: ServerRequest) =
    request.requestPath().contextPath().let {
        ForwardedHeaderUtils
            .adaptFromForwardedHeaders(request.exchange().request.uri, request.exchange().request.headers)
            .replacePath(it.toString())
            .replaceQuery("")
    }
