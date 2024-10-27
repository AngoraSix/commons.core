package com.angorasix.commons.infrastructure.projectmanagement.integrations.dto

import com.angorasix.commons.domain.projectmanagement.integrations.Source

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
data class ContributorSource(
    val source: Source,
    val sourceUserId: String,
)
