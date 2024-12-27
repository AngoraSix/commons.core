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
    val name: String,
    val description: String,
) : A6InfraResourceDto<A6DomainResource.Club>(A6DomainResource.Club)
