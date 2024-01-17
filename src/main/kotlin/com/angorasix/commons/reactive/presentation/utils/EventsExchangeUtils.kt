package com.angorasix.commons.reactive.presentation.utils

import com.angorasix.commons.infrastructure.constants.AngoraSixInfrastructure
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
fun ServerResponse.BodyBuilder.affectedContributors(
    request: ServerRequest,
    affectedContributorsIds: List<String>,
): ServerResponse.BodyBuilder {
    // We'll add the admins only if it's requested (triggers an administered-resource-related event)
    if (request.headers().firstHeader(AngoraSixInfrastructure.TRIGGERS_EVENT_HEADER) == "true"
    ) {
        this.header(
            AngoraSixInfrastructure.EVENT_AFFECTED_CONTRIBUTOR_IDS_HEADER,
            affectedContributorsIds.joinToString(","),
        )
    }
    return this
}
