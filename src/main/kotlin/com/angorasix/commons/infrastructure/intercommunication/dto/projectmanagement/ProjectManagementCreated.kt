package com.angorasix.commons.infrastructure.intercommunication.dto.projectmanagement

import com.angorasix.commons.domain.SimpleContributor
import java.time.Instant

data class ProjectManagementCreated(
    val projectManagementId: String,
    val creatorContributor: SimpleContributor,
    val eventInstant: Instant = Instant.now(),
)
