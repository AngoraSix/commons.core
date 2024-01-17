package com.angorasix.commons.reactive.presentation.filter

import com.angorasix.commons.domain.SimpleContributor
import com.angorasix.commons.infrastructure.constants.AngoraSixInfrastructure
import com.angorasix.commons.infrastructure.oauth2.constants.A6WellKnownClaims
import com.angorasix.commons.reactive.presentation.error.resolveBadRequest
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitPrincipal

/**
 *
 *
 * @author rozagerardo
 */
suspend fun extractRequestingContributor(
    request: ServerRequest,
    next: suspend (ServerRequest) -> ServerResponse,
): ServerResponse {
    val authentication = request.awaitPrincipal() as JwtAuthenticationToken?
    val jwtPrincipal = authentication?.token
    jwtPrincipal?.let {
        val requestingContributor =
            SimpleContributor(
                it.getClaim(A6WellKnownClaims.CONTRIBUTOR_ID),
                authentication.authorities.map { it.authority }.toSet(),
                request.headers()
                    .firstHeader(AngoraSixInfrastructure.REQUEST_IS_ADMIN_HINT_HEADER) == "true",
            )
        request.attributes()[AngoraSixInfrastructure.REQUEST_ATTRIBUTE_CONTRIBUTOR_KEY] =
            requestingContributor
    }
    return next(request)
}

suspend fun extractAffectedContributors(
    request: ServerRequest,
    next: suspend (ServerRequest) -> ServerResponse,
    affectedContributorsIsRequired: Boolean = false,
): ServerResponse {
    val contributorsString = request.headers()
        .firstHeader(AngoraSixInfrastructure.EVENT_AFFECTED_CONTRIBUTOR_IDS_HEADER)
    return if (contributorsString.isNullOrBlank() && affectedContributorsIsRequired) {
        resolveBadRequest(
            "No affected contributors can be resolved from the request header",
            "AFFECTED_CONTRIBUTORS_HEADER",
        )
    } else {
        contributorsString?.let {
            request.attributes()[AngoraSixInfrastructure.REQUEST_ATTRIBUTE_AFFECTED_CONTRIBUTORS_KEY] =
                it.split(",")
        }
        next(request)
    }
}
