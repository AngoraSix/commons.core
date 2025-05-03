package com.angorasix.commons.infrastructure.intercommunication.club

import java.time.Instant

class UserInvited(
    val email: String,
    val club: ClubDetails,
    val token: String,
    val contributorId: String? = null,
    val eventInstant: Instant = Instant.now(),
) {
    data class ClubDetails(
        val id: String,
        val clubType: String,
        val name: String,
        val description: String,
        val projectId: String? = null,
        val managementId: String? = null,
    )
}
