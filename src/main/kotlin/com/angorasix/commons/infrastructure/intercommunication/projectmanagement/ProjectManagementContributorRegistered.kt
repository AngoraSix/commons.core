package com.angorasix.commons.infrastructure.intercommunication.projectmanagement

import java.time.Instant

/**
 * Domain Event
 */
data class ProjectManagementContributorRegistered(
    val projectManagementId: String,
    val registeredContributorId: String,
    val ownershipCurrency: String?,
    val managementFinancialCurrencies: Set<String>,
    val eventInstant: Instant = Instant.now(),
)
