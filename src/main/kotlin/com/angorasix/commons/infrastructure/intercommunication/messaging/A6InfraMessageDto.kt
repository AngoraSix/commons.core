package com.angorasix.commons.infrastructure.intercommunication.messaging

import com.angorasix.commons.domain.A6Contributor
import com.angorasix.commons.infrastructure.intercommunication.A6DomainResource

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
data class A6InfraMessageDto<T>(
    val targetId: String,
//    @JsonSerialize(using = A6DomainResourceSerializer::class)
//    @JsonDeserialize(using = A6DomainResourceDeserializer::class)
    val targetType: A6DomainResource,
    val objectId: String,
    val objectType: String,
    val topic: String,
    val requestingContributor: A6Contributor,
    val messageData: T,
)
//
// // Serializer
// class A6DomainResourceSerializer : JsonSerializer<A6DomainResource>() {
//    override fun serialize(
//        value: A6DomainResource,
//        gen: JsonGenerator,
//        serializers: SerializerProvider,
//    ) {
//        gen.writeString(value.value) // Serialize as a simple string
//    }
// }
//
// // Deserializer
// class A6DomainResourceDeserializer : JsonDeserializer<A6DomainResource>() {
//    override fun deserialize(
//        p: com.fasterxml.jackson.core.JsonParser,
//        ctxt: DeserializationContext,
//    ): A6DomainResource {
//        val value = p.text
//        return A6DomainResource.fromValue(value) // Convert back to A6DomainResource
//    }
// }
