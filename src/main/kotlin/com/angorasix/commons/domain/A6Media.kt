package com.angorasix.commons.domain

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
open class A6Media(
    val mediaType: String,
    val url: String,
    val thumbnailUrl: String,
    val resourceId: String,
)

enum class A6MediaTypes(val value: String) {
    IMAGE("image"),
}
