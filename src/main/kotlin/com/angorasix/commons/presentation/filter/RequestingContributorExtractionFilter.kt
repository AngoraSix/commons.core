package com.angorasix.commons.presentation.filter

import com.angorasix.commons.domain.SimpleContributor
import com.angorasix.commons.infrastructure.constants.AngoraSixInfrastructure
import com.angorasix.commons.infrastructure.oauth2.constants.A6WellKnownClaims
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
            )
        request.attributes()[AngoraSixInfrastructure.REQUEST_ATTRIBUTE_CONTRIBUTOR_KEY] =
            requestingContributor
    }
    return next(request)
}
