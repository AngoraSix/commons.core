package com.angorasix.commons.reactive.presentation.filter

import com.angorasix.commons.domain.A6Contributor
import com.angorasix.commons.domain.A6Media
import com.angorasix.commons.domain.A6MediaTypes
import com.angorasix.commons.infrastructure.constants.AngoraSixInfrastructure
import com.angorasix.commons.infrastructure.oauth2.constants.A6WellKnownClaims
import com.angorasix.commons.reactive.presentation.error.resolveBadRequest
import org.springframework.security.oauth2.core.oidc.StandardClaimNames
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
        val firstName =
            it.getClaimAsString(StandardClaimNames.GIVEN_NAME)
                ?: it.getClaimAsString(StandardClaimNames.NAME)
                ?: it.getClaimAsString(StandardClaimNames.NICKNAME)
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

suspend fun extractAffectedContributors(
    request: ServerRequest,
    next: suspend (ServerRequest) -> ServerResponse,
    affectedContributorsIsRequired: Boolean = false,
): ServerResponse {
    val contributorsString =
        request
            .headers()
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
