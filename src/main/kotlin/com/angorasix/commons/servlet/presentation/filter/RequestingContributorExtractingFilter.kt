package com.angorasix.commons.servlet.presentation.filter

import com.angorasix.commons.domain.SimpleContributor
import com.angorasix.commons.infrastructure.constants.AngoraSixInfrastructure
import com.angorasix.commons.infrastructure.oauth2.constants.A6WellKnownClaims
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.principalOrNull

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
fun extractRequestingContributor(
    request: ServerRequest,
    next: (ServerRequest) -> ServerResponse,
): ServerResponse {
    val authentication = request.principalOrNull() as JwtAuthenticationToken?
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
