package com.angorasix.commons.presentation.filter

import com.angorasix.commons.domain.RequestingContributor
import com.angorasix.commons.infrastructure.presentation.error.resolveUnauthorized
import com.angorasix.commons.presentation.dto.ContributorHeaderDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import java.util.*

/**
 *
 *
 * @author rozagerardo
 */
suspend fun extractRequestingContributor(
    request: ServerRequest,
    next: suspend (ServerRequest) -> ServerResponse,
    contributorHeaderKey: String,
    objectMapper: ObjectMapper,
): ServerResponse {
    request.headers().header(contributorHeaderKey).firstOrNull()?.let {
        val contributorHeaderString = String(Base64.getUrlDecoder().decode(it))
        val contributorHeader = objectMapper.readValue(
            contributorHeaderString,
            ContributorHeaderDto::class.java,
        )
        val requestingContributorToken = RequestingContributor(
            contributorHeader.contributorId,
            contributorHeader.projectAdmin,
        )
        request.attributes()[contributorHeaderKey] = requestingContributorToken
    }
    return next(request)
}

suspend fun checkRequestingContributor(
    request: ServerRequest,
    next: suspend (ServerRequest) -> ServerResponse,
    contributorHeaderKey: String,
    nonAdminRequestAllowed: Boolean = false,
): ServerResponse {
    val requestingContributor = request.attributes()[contributorHeaderKey] as? RequestingContributor
    return if (requestingContributor != null && (requestingContributor.isProjectAdmin || nonAdminRequestAllowed)) {
        next(request)
    } else {
        resolveUnauthorized(
            "Requesting Contributor Header is not present or authorized",
            "Requesting Contributor Header",
        )
    }
}
