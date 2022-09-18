package com.angorasix.commons.presentation.filter

import com.angorasix.commons.domain.RequestingContributor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
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
@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequestingContributorExtractionFilterUnitTest {

    private lateinit var objectMapper: ObjectMapper

    @BeforeAll
    fun setup() {
        objectMapper = ObjectMapper()
        objectMapper.registerModule(KotlinModule.Builder().build())
    }

    @Test
    @Throws(Exception::class)
    fun `Given request with Admin Contributor Header - When extractRequestingContributor invoked - Then response contains attribute`() =
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
                extractRequestingContributor(
                    mockedRequest,
                    next,
                    "Angorasix-API",
                    objectMapper,
                )

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.OK)
            assertThat(mockedRequest.attributes()).containsKey("Angorasix-API")
            val requestingContributor =
                mockedRequest.attributes()["Angorasix-API"] as RequestingContributor
            assertThat(requestingContributor.id).isEqualTo("contrId1")
            assertThat(requestingContributor.isProjectAdmin).isTrue()
        }

    @Test
    @Throws(Exception::class)
    fun `Given request with Admin Contributor Attribute - When checkRequestingContributor invoked - Then response contains attribute and continues`() =
        runTest {
            val contributorAttribute = RequestingContributor("contrId1", true)
            val mockedRequest: ServerRequest = MockServerRequest.builder()
                .attribute("Angorasix-API", contributorAttribute).build()
            val next: suspend (request: ServerRequest) -> ServerResponse = {
                ServerResponse.ok().buildAndAwait()
            }

            val outputResponse =
                checkRequestingContributor(
                    mockedRequest,
                    next,
                    "Angorasix-API",
                )

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.OK)
            assertThat(mockedRequest.attributes()).containsKey("Angorasix-API")
            val requestingContributor =
                mockedRequest.attributes()["Angorasix-API"] as RequestingContributor
            assertThat(requestingContributor).isEqualTo(contributorAttribute)
        }

    @Test
    @Throws(Exception::class)
    fun `Given request with Non Admin Contributor Attribute - When checkRequestingContributor allowing non-Admin invoked - Then response continues`() =
        runTest {
            val contributorAttribute = RequestingContributor("contrId1", false)
            val mockedRequest: ServerRequest = MockServerRequest.builder()
                .attribute("Angorasix-API", contributorAttribute).build()
            val next: suspend (request: ServerRequest) -> ServerResponse = {
                ServerResponse.ok().buildAndAwait()
            }

            val outputResponse =
                checkRequestingContributor(
                    mockedRequest,
                    next,
                    "Angorasix-API",
                    true,
                )

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.OK)
            assertThat(mockedRequest.attributes()).containsKey("Angorasix-API")
            val requestingContributor =
                mockedRequest.attributes()["Angorasix-API"] as RequestingContributor
            assertThat(requestingContributor).isEqualTo(contributorAttribute)
        }

    @Test
    @Throws(Exception::class)
    fun `Given request with Non Admin Contributor Attribute - When checkRequestingContributor invoked - Then response is Unauthorized`() =
        runTest {
            val contributorAttribute = RequestingContributor("contrId1", false)
            val mockedRequest: ServerRequest = MockServerRequest.builder()
                .attribute("Angorasix-API", contributorAttribute).build()
            val next: suspend (request: ServerRequest) -> ServerResponse = {
                ServerResponse.ok().buildAndAwait()
            }

            val outputResponse =
                checkRequestingContributor(
                    mockedRequest,
                    next,
                    "Angorasix-API",
                )

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED)
        }

    @Test
    @Throws(Exception::class)
    fun `Given request without Contributor Attribute - When checkRequestingContributor invoked - Then unauthorized response`() =
        runTest {
            val mockedRequest: ServerRequest = MockServerRequest.builder().build()
            val next: suspend (request: ServerRequest) -> ServerResponse = {
                ServerResponse.ok().buildAndAwait()
            }

            val outputResponse =
                checkRequestingContributor(
                    mockedRequest,
                    next,
                    "Angorasix-API",
                )

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED)
        }

    @Test
    @Throws(Exception::class)
    fun `Given request without Contributor Attribute - When checkRequestingContributor allowing non-Admin invoked - Then unauthorized response`() =
        runTest {
            val mockedRequest: ServerRequest = MockServerRequest.builder().build()
            val next: suspend (request: ServerRequest) -> ServerResponse = {
                ServerResponse.ok().buildAndAwait()
            }

            val outputResponse =
                checkRequestingContributor(
                    mockedRequest,
                    next,
                    "Angorasix-API",
                    true,
                )

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED)
        }
}
