package com.angorasix.commons.presentation.dto

import com.angorasix.commons.domain.RequestingContributor
import com.angorasix.commons.domain.modification.DomainObjectModification
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
class Patch @JsonCreator constructor(@get:JsonValue val operations: List<PatchOperation>)

data class PatchOperation(val op: String, val path: String, val value: JsonNode?) {
    fun toDomainObjectModification(
        contributor: RequestingContributor,
        supportedOperations: List<PatchOperationSpec>,
        objectMapper: ObjectMapper,
    ): DomainObjectModification<out Any, out Any> {
        return supportedOperations.find { it.supportsPatchOperation(this) }
            ?.mapToObjectModification(contributor, this, objectMapper)
            ?: throw IllegalArgumentException("Patch Operation not supported")
    }
}

interface PatchOperationSpec {
    fun supportsPatchOperation(operation: PatchOperation): Boolean
    fun mapToObjectModification(
        contributor: RequestingContributor,
        operation: PatchOperation,
        objectMapper: ObjectMapper,
    ): DomainObjectModification<out Any, out Any>
}
