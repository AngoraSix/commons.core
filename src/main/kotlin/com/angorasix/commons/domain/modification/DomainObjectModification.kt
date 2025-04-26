package com.angorasix.commons.domain.modification

import com.angorasix.commons.domain.SimpleContributor

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
        simpleContributor: SimpleContributor,
        domainObject: T,
    ): T
}
