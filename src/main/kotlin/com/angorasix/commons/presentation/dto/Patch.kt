package com.angorasix.commons.presentation.dto

import com.angorasix.commons.domain.SimpleContributor
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
        contributor: SimpleContributor,
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
        contributor: SimpleContributor,
        operation: PatchOperation,
        objectMapper: ObjectMapper,
    ): DomainObjectModification<out Any, out Any>
}

class BulkPatch @JsonCreator constructor(@get:JsonValue val operations: List<BulkPatchOperation>)

data class BulkPatchOperation(val op: String, val path: String, val value: JsonNode?) {
    fun toBulkModificationStrategy(
        contributor: SimpleContributor,
        supportedOperations: List<BulkPatchOperationSpec>,
    ): String {
        return supportedOperations.find { it.supportsPatchOperation(this) }
            ?.mapToStrategyId(contributor, this)
            ?: throw IllegalArgumentException("Bulk Patch Operation not supported")
    }
}

interface BulkPatchOperationSpec {
    fun supportsPatchOperation(operation: BulkPatchOperation): Boolean
    fun mapToStrategyId(
        contributor: SimpleContributor,
        operation: BulkPatchOperation,
    ): String
}
