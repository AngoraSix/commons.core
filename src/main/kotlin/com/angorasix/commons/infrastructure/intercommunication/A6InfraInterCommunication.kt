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
}

enum class A6InfraTopics(val value: String) {
    ADD_MEMBER("addMember"),
    REMOVE_MEMBER("removeMember"),
}
