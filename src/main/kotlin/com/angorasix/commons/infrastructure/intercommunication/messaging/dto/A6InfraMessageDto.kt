package com.angorasix.commons.infrastructure.intercommunication.messaging.dto

import com.angorasix.commons.domain.DetailedContributor
import com.angorasix.commons.infrastructure.intercommunication.A6DomainResource

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
data class A6InfraMessageDto(
    val targetId: String,
    val targetType: A6DomainResource,
    val objectId: String,
    val objectType: String,
    val topic: String,
    val requestingContributor: DetailedContributor,
    val messageData: Map<String, Any>,
)
