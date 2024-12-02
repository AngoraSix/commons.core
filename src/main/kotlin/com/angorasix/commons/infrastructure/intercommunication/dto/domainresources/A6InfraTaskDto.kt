package com.angorasix.commons.infrastructure.intercommunication.dto.domainresources

import com.angorasix.commons.infrastructure.intercommunication.dto.A6DomainResource
import java.time.Instant

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
data class A6InfraTaskDto(
    val integrationId: String,
    val title: String,
    val description: String? = null,
    val dueInstant: Instant? = null,
    val assigneeIds: Set<String> = emptySet(),
    val done: Boolean = false,
    val sourceType: String? = null,
    val angorasixId: String? = null,
) : A6InfraResourceDto<A6DomainResource.Task>(A6DomainResource.Task)
