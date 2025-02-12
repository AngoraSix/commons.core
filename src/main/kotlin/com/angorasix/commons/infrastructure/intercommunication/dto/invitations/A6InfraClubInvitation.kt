package com.angorasix.commons.infrastructure.intercommunication.dto.invitations

import com.angorasix.commons.infrastructure.intercommunication.dto.domainresources.A6InfraClubDto

class A6InfraClubInvitation(
    val email: String,
    val club: A6InfraClubDto,
    val token: String,
    val contributorId: String? = null,
)
