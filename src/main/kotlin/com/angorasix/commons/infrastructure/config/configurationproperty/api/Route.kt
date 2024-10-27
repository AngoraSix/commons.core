package com.angorasix.commons.infrastructure.config.configurationproperty.api

import org.springframework.http.HttpMethod

data class Route(
    val name: String,
    val basePaths: List<String>,
    val method: HttpMethod,
    val path: String,
) {
    fun resolvePath(): String = basePaths.joinToString("").plus(path)
}
