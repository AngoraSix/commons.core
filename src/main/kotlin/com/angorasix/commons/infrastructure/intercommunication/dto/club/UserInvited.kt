package com.angorasix.commons.infrastructure.intercommunication.dto.club

import com.angorasix.commons.infrastructure.intercommunication.dto.domainresources.A6InfraClubDto
import java.time.Instant

class UserInvited(
    val email: String,
    val club: A6InfraClubDto,
    val token: String,
    val contributorId: String? = null,
    val eventInstant: Instant = Instant.now(),
)

fun UserInvited.toMap(): Map<String, Any> =
    buildMap {
        put("email", email)
        put("club", club)
        put("token", token)
        put("eventInstant", eventInstant)
        contributorId?.let { put("contributorId", it) }
    }
