package com.angorasix.commons.infrastructure.intercommunication.tasks

data class TasksSyncingCorrespondenceProcessed(
    val collection: List<TaskSyncingCorrespondence>,
) {
    data class TaskSyncingCorrespondence(
        val integrationId: String,
        val a6Id: String,
    )
}
