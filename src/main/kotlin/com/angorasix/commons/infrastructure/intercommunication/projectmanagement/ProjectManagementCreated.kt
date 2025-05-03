package com.angorasix.commons.infrastructure.intercommunication.projectmanagement

import com.angorasix.commons.domain.A6Contributor
import java.time.Instant

data class ProjectManagementCreated(
    val projectManagementId: String,
    val creatorContributor: A6Contributor,
    val eventInstant: Instant = Instant.now(),
)
