package com.angorasix.commons.infrastructure.intercommunication

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
enum class A6DomainResource(
    val value: String,
) {
    CONTRIBUTOR("contributor"),
    TASK("task"),
    CLUB("club"),
    PROJECT("project"),
    PROJECT_MANAGEMENT("projectManagement"),
    SOURCE_SYNC("sourceSync"),
    INTEGRATION_SOURCE_SYNC_EVENT("integrationSourceSyncEvent"),

//    @JsonCreator
//    fun fromValue(value: String): A6DomainResource =
//        when (value) {
//            CONTRIBUTOR.value -> CONTRIBUTOR
//            CLUB.value -> CLUB
//            PROJECT.value -> PROJECT
//            PROJECT_MANAGEMENT.value -> PROJECT_MANAGEMENT
//            TASK.value -> TASK
//            SOURCE_SYNC.value -> SOURCE_SYNC
//            else -> throw IllegalArgumentException("Unknown value: $value")
//        }
}

enum class A6InfraTopics(
    val value: String,
) {
    // CLUB
    PROJECT_CLUB_MEMBER_JOINED("projectClubMemberJoined"),
    MANAGEMENT_CLUB_MEMBER_JOINED("managementClubMemberJoined"),
    CLUB_INVITATION("clubInvitation"),

    // PROJECT
    PROJECT_CREATED("projectCreated"),

    // INTEGRATIONS
    TASKS_INTEGRATION_FULL_SYNCING("tasksIntegrationFullSyncing"),
    TASKS_INTEGRATION_SYNCING_CORRESPONDENCE("tasksIntegrationSyncingCorrespondence"),

    // TASKS
    TASKS_CLOSED("tasksClosed"),

    // PROJECT MANAGEMENT
    PROJECT_MANAGEMENT_CREATED("projectManagementCreated"),
    PROJECT_MANAGEMENT_CONTRIBUTOR_REGISTERED("projectManagementContributorRegistered"),
    PROJECT_MANAGEMENT_TASKS_CLOSED("projectManagementTasksClosed"),
}

const val A6_INFRA_BULK_ID = "a6-bulk-collection" // messages with this id should have a "collection" field
