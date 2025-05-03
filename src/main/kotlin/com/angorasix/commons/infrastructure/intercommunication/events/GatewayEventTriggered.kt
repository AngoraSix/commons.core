package com.angorasix.commons.infrastructure.intercommunication.events

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
class GatewayEventTriggered(
    val subjectType: String,
    val subjectId: String,
    val subjectEvent: String, // should match A6InfraTopics, not enforcing to avoid issues
    val eventData: Map<String, Any>,
)
