package com.angorasix.commons.servlet.presentation.filter

import com.angorasix.commons.domain.A6Contributor
import com.angorasix.commons.domain.A6Media
import com.angorasix.commons.domain.A6MediaTypes
import com.angorasix.commons.infrastructure.constants.AngoraSixInfrastructure
import com.angorasix.commons.infrastructure.oauth2.constants.A6WellKnownClaims
import org.springframework.security.oauth2.core.oidc.StandardClaimNames
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
        val firstName =
            it.getClaimAsString(StandardClaimNames.GIVEN_NAME) ?: it.getClaimAsString(
                StandardClaimNames.NAME,
            )
        val requestingContributor =
            A6Contributor(
                it.getClaim(A6WellKnownClaims.CONTRIBUTOR_ID),
                it.getClaim(StandardClaimNames.EMAIL),
                firstName,
                it.getClaim(StandardClaimNames.FAMILY_NAME),
                it.getClaimAsString(StandardClaimNames.PICTURE)?.let { pictureUrl ->
                    A6Media(
                        A6MediaTypes.IMAGE.value,
                        pictureUrl,
                        pictureUrl,
                        pictureUrl,
                    )
                },
                request
                    .headers()
                    .firstHeader(AngoraSixInfrastructure.REQUEST_IS_ADMIN_HINT_HEADER) == "true",
            )
        request.attributes()[AngoraSixInfrastructure.REQUEST_ATTRIBUTE_CONTRIBUTOR_KEY] =
            requestingContributor
    }
    return next(request)
}
