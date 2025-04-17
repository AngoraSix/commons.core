package com.angorasix.commons.presentation.dto.projectmanagement

import com.angorasix.commons.domain.projectmanagement.core.Bylaw

data class BylawDto(
    val scope: String,
    val definition: Any,
)

fun Bylaw<Any>.convertToDto(): BylawDto = BylawDto(scope, definition)

fun BylawDto.convertToDomain(): Bylaw<Any> = Bylaw(scope, definition)
