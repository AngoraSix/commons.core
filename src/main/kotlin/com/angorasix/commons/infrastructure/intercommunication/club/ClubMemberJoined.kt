package com.angorasix.commons.infrastructure.intercommunication.club

import java.time.Instant

class ClubMemberJoined(
    val joinedMemberContributorId: String,
    val club: ClubDetails,
    val eventInstant: Instant = Instant.now(),
) {
    data class ClubDetails(
        val id: String,
        val clubType: String,
        val projectId: String? = null,
        val managementId: String? = null,
    )
}
