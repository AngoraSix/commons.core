package com.angorasix.commons.presentation.handler

import com.angorasix.commons.domain.A6Media
import com.angorasix.commons.presentation.dto.A6MediaDto

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
fun A6Media.convertToDto(): A6MediaDto =
    A6MediaDto(
        mediaType,
        url,
        thumbnailUrl,
        resourceId,
    )
