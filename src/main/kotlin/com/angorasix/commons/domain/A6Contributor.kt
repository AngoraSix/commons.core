package com.angorasix.commons.domain

import org.springframework.data.annotation.Transient

data class A6Contributor(
    val contributorId: String,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val profileMedia: A6Media? = null,
    @Transient val isAdminHint: Boolean? = false,
)
