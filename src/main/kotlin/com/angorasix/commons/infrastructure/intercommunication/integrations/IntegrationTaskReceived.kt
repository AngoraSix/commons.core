package com.angorasix.commons.infrastructure.intercommunication.integrations

import java.time.Instant

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
data class IntegrationTaskReceived(
    val collection: List<IntegrationTask>,
) {
    data class IntegrationTask(
        val integrationId: String,
        val title: String,
        val description: String? = null,
        val dueInstant: Instant? = null,
        val assigneeIds: Set<String> = emptySet(),
        val done: Boolean = false,
        val sourceType: String? = null,
        val a6Id: String? = null,
        val estimations: IntegrationTaskEstimation? = null,
    )

    data class IntegrationTaskEstimation(
        val caps: Double?,
        val strategy: String?,
        val effort: Double?,
        val complexity: Double?,
        val industry: String?,
        val industryModifier: Double?,
        val moneyPayment: Double?,
    )
}
