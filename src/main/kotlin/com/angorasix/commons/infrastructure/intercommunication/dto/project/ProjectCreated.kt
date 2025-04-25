package com.angorasix.commons.infrastructure.intercommunication.dto.project

import com.angorasix.commons.domain.SimpleContributor
import java.time.Instant

data class ProjectCreated(
    val projectId: String,
    val creatorContributor: SimpleContributor,
    val eventInstant: Instant = Instant.now(),
)

fun ProjectCreated.toMap(): Map<String, Any> =
    buildMap {
        put("projectId", projectId)
        put("creatorContributor", creatorContributor)
        put("eventInstant", eventInstant)
    }
