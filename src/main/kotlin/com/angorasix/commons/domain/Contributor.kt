package com.angorasix.commons.domain

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.annotation.Transient

// Note: to access DetailedContributor data, use e.g. val email = (base as? DetailedContributor)?.email
// 1) Annotate the base class with JsonTypeInfo/JsonSubTypes
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "kind",
)
@JsonSubTypes(
    JsonSubTypes.Type(SimpleContributor::class, name = "simple"),
    JsonSubTypes.Type(DetailedContributor::class, name = "detailed"),
)
open class SimpleContributor(
    open val contributorId: String,
    open val grants: Set<String> = emptySet(),
    @Transient open val isAdminHint: Boolean? = false,
) {
    @PersistenceCreator
    constructor(
        contributorId: String,
        grants: Set<String> = emptySet(),
    ) : this(contributorId, grants, null)
}

// 2) (Optional) give the subtype an explicit JsonTypeName
@JsonTypeName("detailed")
class DetailedContributor
    @PersistenceCreator
    constructor(
        contributorId: String,
        grants: Set<String> = emptySet(),
        isAdminHint: Boolean? = false,
        val email: String? = null,
        val firstName: String? = null,
        val lastName: String? = null,
        val profileMedia: A6Media? = null,
    ) : SimpleContributor(contributorId, grants, isAdminHint)
