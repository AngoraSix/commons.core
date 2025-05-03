package com.angorasix.commons.infrastructure.intercommunication.project

import com.angorasix.commons.domain.A6Contributor
import java.time.Instant

data class ProjectCreated(
    val projectId: String,
    val creatorContributor: A6Contributor,
    val eventInstant: Instant = Instant.now(),
)
