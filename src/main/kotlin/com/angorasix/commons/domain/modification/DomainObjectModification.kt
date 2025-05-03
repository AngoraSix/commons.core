package com.angorasix.commons.domain.modification

import com.angorasix.commons.domain.A6Contributor

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
abstract class DomainObjectModification<T, U>(
    val modifyValue: U,
) {
    abstract fun modify(
        requestingContributor: A6Contributor,
        domainObject: T,
    ): T
}
