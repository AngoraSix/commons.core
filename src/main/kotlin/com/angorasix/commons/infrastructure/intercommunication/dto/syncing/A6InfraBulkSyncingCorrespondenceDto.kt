package com.angorasix.commons.infrastructure.intercommunication.dto.syncing

import com.angorasix.commons.infrastructure.intercommunication.dto.A6DomainResource
import com.angorasix.commons.infrastructure.intercommunication.dto.messaging.A6DomainResourceDeserializer
import com.angorasix.commons.infrastructure.intercommunication.dto.messaging.A6DomainResourceSerializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

data class A6InfraBulkSyncingCorrespondenceDto(
    val sourceType: String,
    @JsonSerialize(using = A6DomainResourceSerializer::class)
    @JsonDeserialize(using = A6DomainResourceDeserializer::class)
    val a6Type: A6DomainResource,
    val collection: List<A6InfraSyncingCorrespondenceDto>,
) {
    fun toMap(): Map<String, Any> =
        mapOf(
            "sourceType" to sourceType,
            "a6Type" to a6Type.value,
            "collection" to collection,
        )
}
