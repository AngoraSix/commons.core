package com.angorasix.commons.infrastructure.intercommunication.projectmanagement

import java.time.Duration
import java.time.Instant

data class ManagementTasksClosed(
    val projectManagementId: String,
    val collection: List<ManagementTaskClosed>,
    val ownershipCurrency: String?,
    val managementFinancialCurrencies: Set<String>,
    val currencyDistributionRules: Map<String, TasksDistributionRules>, // different rules for each currency
) {
    data class ManagementTaskClosed(
        val taskId: String,
        val assigneeContributorIds: Set<String>,
        val doneInstant: Instant,
        val moneyPayment: Double? = null,
        val caps: Double? = null,
    )

    data class TasksDistributionRules(
        val startupDefaultDuration: Duration,
        val regularDefaultDuration: Duration,
    )
}
