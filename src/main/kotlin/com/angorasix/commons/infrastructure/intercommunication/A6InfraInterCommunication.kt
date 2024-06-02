package com.angorasix.commons.infrastructure.intercommunication

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
enum class A6DomainResource(val value: String) {
    CONTRIBUTOR("contributor"),
    CLUB("club"),
    PROJECT_MANAGEMENT("project-management"),
    PROJECT_MANAGEMENT_INTEGRATION_SOURCE("project-management-integration-source"),
}

enum class A6InfraTopics(val value: String) {
    ADD_MEMBER("addMember"),
    REMOVE_MEMBER("removeMember"),

    // MGMT
    MGMT_TASKS_UPDATE("updateProjectsManagementTasks"),
}
