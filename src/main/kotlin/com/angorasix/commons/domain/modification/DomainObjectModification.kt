package com.angorasix.commons.domain.modification

import com.angorasix.commons.domain.RequestingContributor

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
abstract class DomainObjectModification<T, U>(val modifyValue: U) {
    abstract fun modify(requestingContributor: RequestingContributor, domainObject: T): T
}
