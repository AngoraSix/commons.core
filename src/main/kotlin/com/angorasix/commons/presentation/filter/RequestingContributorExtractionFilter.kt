package com.angorasix.commons.presentation.filter

import com.angorasix.commons.domain.RequestingContributor
import com.angorasix.commons.presentation.dto.ContributorHeaderDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.status
import org.springframework.web.reactive.function.server.buildAndAwait
import java.util.*

/**
 *
 *
 * @author rozagerardo
 */
suspend fun resolveRequestingContributor(
    request: ServerRequest,
    next: suspend (ServerRequest) -> ServerResponse,
    contributorHeaderKey: String,
    objectMapper: ObjectMapper,
    anonymousRequestAllowed: Boolean = false
): ServerResponse {
    request.headers().header(contributorHeaderKey).firstOrNull()?.let {
        val contributorHeaderString = String(Base64.getUrlDecoder().decode(it))
        val contributorHeader = objectMapper.readValue(
            contributorHeaderString,
            ContributorHeaderDto::class.java
        )
        val requestingContributorToken = RequestingContributor(
            contributorHeader.contributorId,
            contributorHeader.projectAdmin
        )
        request.attributes()[contributorHeaderKey] = requestingContributorToken
        return next(request)
    }
    return if (anonymousRequestAllowed) {
        next(request)
    } else {
        status(HttpStatus.UNAUTHORIZED).buildAndAwait()
    }
}
