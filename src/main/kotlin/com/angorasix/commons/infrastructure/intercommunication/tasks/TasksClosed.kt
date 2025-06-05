package com.angorasix.commons.infrastructure.intercommunication.tasks

import java.time.Instant

data class TasksClosed(
    val projectManagementId: String,
    val collection: List<TaskClosed>,
) {
    data class TaskClosed(
        val taskId: String,
        val assigneeContributorIds: Set<String>,
        val doneInstant: Instant,
        val moneyPayment: Double? = null,
        val caps: Double? = null,
    )
}
