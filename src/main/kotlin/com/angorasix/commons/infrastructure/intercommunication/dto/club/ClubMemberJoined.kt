package com.angorasix.commons.infrastructure.intercommunication.dto.club

import com.angorasix.commons.infrastructure.intercommunication.dto.domainresources.A6InfraClubDto
import java.time.Instant

class ClubMemberJoined(
    val joinedMemberContributorId: String,
    val club: A6InfraClubDto,
    val eventInstant: Instant = Instant.now(),
)
