package com.angorasix.commons.infrastructure.intercommunication.dto.domainresources

import com.angorasix.commons.infrastructure.intercommunication.dto.A6DomainResource

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
data class A6InfraBulkResourceDto<T : A6DomainResource>(
    val type: T,
    val collection: List<A6InfraResourceDto<T>>,
) {
    fun toMap(): Map<String, Any> =
        mapOf(
            "type" to type.value,
            "collection" to collection,
        )

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun <T : A6DomainResource> fromMap(map: Map<String, Any>): A6InfraBulkResourceDto<T> =
            A6InfraBulkResourceDto(
                A6DomainResource.fromValue(map["type"] as String) as T,
                map["collection"] as List<A6InfraResourceDto<T>>,
            )
    }
}
