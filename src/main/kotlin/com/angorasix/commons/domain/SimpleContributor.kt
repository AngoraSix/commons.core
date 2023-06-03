package com.angorasix.commons.domain

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
data class SimpleContributor constructor(
    val id: String,
    val grants: Set<String> = emptySet(),
)
