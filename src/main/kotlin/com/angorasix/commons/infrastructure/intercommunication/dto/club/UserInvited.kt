package com.angorasix.commons.infrastructure.intercommunication.dto.club

import com.angorasix.commons.infrastructure.intercommunication.dto.domainresources.A6InfraClubDto

class UserInvited(
    val email: String,
    val club: A6InfraClubDto,
    val token: String,
    val contributorId: String? = null,
)
