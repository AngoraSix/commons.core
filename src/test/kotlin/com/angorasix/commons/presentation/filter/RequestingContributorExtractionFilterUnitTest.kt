package com.angorasix.commons.presentation.filter

import com.angorasix.commons.domain.RequestingContributor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.http.HttpStatus
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.buildAndAwait
import java.util.*

/**
 * @author rozagerardo
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RequestingContributorExtractionFilterUnitTest {

    private lateinit var objectMapper: ObjectMapper

    @BeforeAll
    fun setup() {
        objectMapper = ObjectMapper()
        objectMapper.registerModule(KotlinModule.Builder().build())
    }

    @Test
    @Throws(Exception::class)
    fun `Given request - When headerFilterFunction invoked - Then response contains attribute`() =
        runTest {
            val contributorHeaderJson =
                """
            {
                "contributorId": "contrId1",
                "projectAdmin": true
            }
                """.trimIndent()
            val contributorHeaderEncoded =
                Base64.getUrlEncoder().encodeToString(contributorHeaderJson.toByteArray())
            val mockedRequest: ServerRequest = MockServerRequest.builder()
                .header("Angorasix-API", contributorHeaderEncoded).build()
            val next: suspend (request: ServerRequest) -> ServerResponse = {
                ServerResponse.ok().buildAndAwait()
            }

            val outputResponse =
                resolveRequestingContributor(
                    mockedRequest,
                    next,
                    "Angorasix-API",
                    objectMapper
                )

            AssertionsForClassTypes.assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.OK)
            assertThat(mockedRequest.attributes()).containsKey("Angorasix-API")
            val requestingContributor =
                mockedRequest.attributes()["Angorasix-API"] as RequestingContributor
            assertThat(requestingContributor.id).isEqualTo("contrId1")
            assertThat(requestingContributor.isProjectAdmin).isTrue()
        }

    @Test
    @Throws(Exception::class)
    fun `Given request with no header - When headerFilterFunction invoked and header is required - Then response is 401`() =
        runTest {
            val mockedRequest: ServerRequest = MockServerRequest.builder().build()
            val next: suspend (request: ServerRequest) -> ServerResponse = {
                ServerResponse.ok().buildAndAwait()
            }

            val outputResponse =
                resolveRequestingContributor(
                    mockedRequest,
                    next,
                    "Angorasix-API",
                    objectMapper
                )

            AssertionsForClassTypes.assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED)
        }

    @Test
    @Throws(Exception::class)
    fun `Given request with no header - When headerFilterFunction invoked and header is not required - Then response is OK`() =
        runTest {
            val mockedRequest: ServerRequest = MockServerRequest.builder().build()
            val next: suspend (request: ServerRequest) -> ServerResponse = {
                ServerResponse.ok().buildAndAwait()
            }

            val outputResponse =
                resolveRequestingContributor(
                    mockedRequest,
                    next,
                    "Angorasix-API",
                    objectMapper,
                    true
                )

            AssertionsForClassTypes.assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.OK)
        }
}
