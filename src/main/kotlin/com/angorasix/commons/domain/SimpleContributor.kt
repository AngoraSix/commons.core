package com.angorasix.commons.domain

import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.annotation.Transient

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
data class SimpleContributor(
    val contributorId: String,
    val grants: Set<String> = emptySet(),
    @Transient val isAdminHint: Boolean? = false,
) {
    @PersistenceCreator
    constructor(
        contributorId: String,
        grants: Set<String> = emptySet(),
    ) : this(contributorId, grants, null)
}
