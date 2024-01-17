package com.angorasix.commons.presentation.handler

import com.angorasix.commons.domain.A6Media
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommonHandlerFunctionsUnitTest {

    @Test
    @Throws(Exception::class)
    fun `Given A6Media - When convertToDto - Then A6MEdiaDto is retrieved`() =
        runTest {
            val modelMedia = A6Media(
                "image",
                "http://any.url.image.jpg",
                "http://any.url.image-thumbnail.jpg",
                "any-resourceId",
            )

            val dto = modelMedia.convertToDto()

            assertThat(dto.mediaType).isEqualTo("image")
            assertThat(dto.url).isEqualTo("http://any.url.image.jpg")
            assertThat(dto.thumbnailUrl).isEqualTo("http://any.url.image-thumbnail.jpg")
            assertThat(dto.resourceId).isEqualTo("any-resourceId")
        }
}
