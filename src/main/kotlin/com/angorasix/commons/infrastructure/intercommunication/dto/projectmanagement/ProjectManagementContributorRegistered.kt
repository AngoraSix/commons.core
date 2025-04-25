package com.angorasix.commons.infrastructure.intercommunication.dto.projectmanagement

import com.angorasix.commons.presentation.dto.projectmanagement.BylawDto
import java.time.Instant

/**
 * Domain Event
 */
data class ProjectManagementContributorRegistered(
    val projectManagementId: String,
    val registeredContributorId: String,
    val relevantProjectManagementBylaws: List<BylawDto>,
    val eventInstant: Instant,
)
