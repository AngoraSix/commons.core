package com.angorasix.commons.infrastructure.intercommunication.messaging.dto

import com.angorasix.commons.domain.SimpleContributor
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
    val requestingContributor: SimpleContributor,
    val messageData: Map<String, Any>,
)
