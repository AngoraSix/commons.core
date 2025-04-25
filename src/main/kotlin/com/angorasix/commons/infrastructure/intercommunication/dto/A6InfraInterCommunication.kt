package com.angorasix.commons.infrastructure.intercommunication.dto

import com.fasterxml.jackson.annotation.JsonCreator

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
sealed class A6DomainResource(
    val value: String,
) {
    data object Contributor : A6DomainResource("contributor")

    data object Club : A6DomainResource("club")

    data object Project : A6DomainResource("project")

    data object ProjectManagement : A6DomainResource("project-management")

    data object Task : A6DomainResource("task")

    data object IntegrationSourceSync : A6DomainResource("sourceSync")

    data object ProjectManagementIntegrationSource :
        A6DomainResource("project-management-integration-source")

    companion object {
        @JsonCreator
        fun fromValue(value: String): A6DomainResource =
            when (value) {
                Contributor.value -> Contributor
                Club.value -> Club
                Project.value -> Project
                ProjectManagement.value -> ProjectManagement
                ProjectManagementIntegrationSource.value -> ProjectManagementIntegrationSource
                Task.value -> Task
                IntegrationSourceSync.value -> IntegrationSourceSync
                else -> throw IllegalArgumentException("Unknown value: $value")
            }
    }
}

enum class A6InfraTopics(
    val value: String,
) {
    ADD_MEMBER("addMember"),
    REMOVE_MEMBER("removeMember"),

    // PROJECT
    PROJECT_CREATED("projectCreated"),

    // INTEGRATIONS
    TASKS_INTEGRATION_FULL_SYNCING("tasksIntegrationFullSyncing"),
    TASKS_INTEGRATION_SYNCING_CORRESPONDENCE("tasksIntegrationSyncingCorrespondence"),

    // INTEGRATIONS
    CLUB_INVITATION("clubInvitation"),

    PROJECT_MANAGEMENT_CONTRIBUTOR_REGISTERED("projectManagementContributorRegistered"),
}
