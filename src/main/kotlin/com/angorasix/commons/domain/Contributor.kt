package com.angorasix.commons.domain

import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.annotation.Transient

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
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

data class DetailedContributor(
    override val contributorId: String,
    override val grants: Set<String> = emptySet(),
    @Transient override val isAdminHint: Boolean? = false,
    val firstName: String?,
    val lastName: String?,
    val profileMedia: A6Media?,
) : SimpleContributor(contributorId, grants, isAdminHint)
