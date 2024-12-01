package com.angorasix.commons.infrastructure.intercommunication.dto.domainresources

import com.angorasix.commons.infrastructure.intercommunication.dto.A6DomainResource

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
open class A6InfraResourceDto<T : A6DomainResource>(
    val type: T,
)
