package com.angorasix.commons.infrastructure.intercommunication.dto.domainresources

import com.angorasix.commons.infrastructure.intercommunication.dto.A6DomainResource

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
data class A6InfraClubDto(
    val id: String,
    val clubType: String,
    val name: String,
    val description: String,
    val projectId: String? = null,
    val managementId: String? = null,
) : A6InfraResourceDto<A6DomainResource.Club>(A6DomainResource.Club)
