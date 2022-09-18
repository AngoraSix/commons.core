package com.angorasix.commons.infrastructure.presentation.error

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.http.HttpStatus
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.result.view.ViewResolver
import java.util.*

/**
 * @author rozagerardo
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResponseErrorHandlerUnitTest {

    private lateinit var objectMapper: ObjectMapper

    @BeforeAll
    fun setup() {
        objectMapper = ObjectMapper()
        objectMapper.registerModule(KotlinModule.Builder().build())
    }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `Given IllegalArgumentException - When resolveExceptionResponse - Then response is 400`() =
        runTest {
            val outputResponse =
                resolveExceptionResponse(IllegalArgumentException("Mocked Error Message"))

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST)

            val errorResponseBody = fetchBodyAsErrorResponseBody(outputResponse)

            assertThat(errorResponseBody.error).isEqualTo("IllegalArgumentException")
            assertThat(errorResponseBody.errorCode).isEqualTo("ELEMENT_INVALID")
            assertThat(errorResponseBody.message).isEqualTo("Mocked Error Message")
            assertThat(errorResponseBody.status).isEqualTo(400)
        }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `Given IllegalArgumentException - When resolveExceptionResponse defining Element - Then response is 400 with customized fields`() =
        runTest {
            val outputResponse =
                resolveExceptionResponse(
                    IllegalArgumentException("Mocked Error Message"),
                    "Mock",
                )

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST)

            val errorResponseBody = fetchBodyAsErrorResponseBody(outputResponse)

            assertThat(errorResponseBody.error).isEqualTo("IllegalArgumentException")
            assertThat(errorResponseBody.errorCode).isEqualTo("MOCK_INVALID")
            assertThat(errorResponseBody.message).isEqualTo("Mocked Error Message")
            assertThat(errorResponseBody.status).isEqualTo(400)
        }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `Given Exception - When resolveExceptionResponse defining Element - Then response is 500 with customized fields`() =
        runTest {
            val outputResponse =
                resolveExceptionResponse(Exception("Mocked Error Message"), "Mock")

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)

            val errorResponseBody = fetchBodyAsErrorResponseBody(outputResponse)

            assertThat(errorResponseBody.error).isEqualTo("Exception")
            assertThat(errorResponseBody.errorCode).isEqualTo("MOCK_ERROR")
            assertThat(errorResponseBody.message).isEqualTo("Mocked Error Message")
            assertThat(errorResponseBody.status).isEqualTo(500)
        }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When resolveNotFound - Then response is 404 with standard fields`() = runTest {
        val outputResponse = resolveNotFound()

        assertThat(outputResponse.statusCode())
            .isEqualTo(HttpStatus.NOT_FOUND)

        val errorResponseBody = fetchBodyAsErrorResponseBody(outputResponse)

        assertThat(errorResponseBody.error).isEqualTo("ElementNotFound")
        assertThat(errorResponseBody.errorCode).isEqualTo("ELEMENT_NOT_FOUND")
        assertThat(errorResponseBody.message).isEqualTo("Element not found")
        assertThat(errorResponseBody.status).isEqualTo(404)
    }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When resolveNotFound defining Element and message - Then response is 404 with customized fields`() =
        runTest {
            val outputResponse = resolveNotFound("Mocked Error Message", "Mock")

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.NOT_FOUND)

            val errorResponseBody = fetchBodyAsErrorResponseBody(outputResponse)

            assertThat(errorResponseBody.error).isEqualTo("ElementNotFound")
            assertThat(errorResponseBody.errorCode).isEqualTo("MOCK_NOT_FOUND")
            assertThat(errorResponseBody.message).isEqualTo("Mocked Error Message")
            assertThat(errorResponseBody.status).isEqualTo(404)
        }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When resolveBadRequest - Then response is 400 with standard fields`() = runTest {
        val outputResponse = resolveBadRequest()

        assertThat(outputResponse.statusCode())
            .isEqualTo(HttpStatus.BAD_REQUEST)

        val errorResponseBody = fetchBodyAsErrorResponseBody(outputResponse)

        assertThat(errorResponseBody.error).isEqualTo("ElementInvalid")
        assertThat(errorResponseBody.errorCode).isEqualTo("ELEMENT_INVALID")
        assertThat(errorResponseBody.message).isEqualTo("Element is invalid")
        assertThat(errorResponseBody.status).isEqualTo(400)
    }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When resolveBadRequest defining Element and message - Then response is 400 with customized fields`() =
        runTest {
            val outputResponse = resolveBadRequest("Mocked Error Message", "Mock")

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST)

            val errorResponseBody = fetchBodyAsErrorResponseBody(outputResponse)

            assertThat(errorResponseBody.error).isEqualTo("ElementInvalid")
            assertThat(errorResponseBody.errorCode).isEqualTo("MOCK_INVALID")
            assertThat(errorResponseBody.message).isEqualTo("Mocked Error Message")
            assertThat(errorResponseBody.status).isEqualTo(400)
        }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When resolveUnauthorized - Then response is 401 with standard fields`() = runTest {
        val outputResponse = resolveUnauthorized()

        assertThat(outputResponse.statusCode())
            .isEqualTo(HttpStatus.UNAUTHORIZED)

        val errorResponseBody = fetchBodyAsErrorResponseBody(outputResponse)

        assertThat(errorResponseBody.error).isEqualTo("UnauthorizedDueToElement")
        assertThat(errorResponseBody.errorCode).isEqualTo("ELEMENT_UNAUTHORIZED")
        assertThat(errorResponseBody.message).isEqualTo("Element is invalid")
        assertThat(errorResponseBody.status).isEqualTo(401)
    }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When resolveUnauthorized defining Element and message - Then response is 401 with customized fields`() =
        runTest {
            val outputResponse = resolveUnauthorized("Mocked Error Message", "Mock")

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED)

            val errorResponseBody = fetchBodyAsErrorResponseBody(outputResponse)

            assertThat(errorResponseBody.error).isEqualTo("UnauthorizedDueToElement")
            assertThat(errorResponseBody.errorCode).isEqualTo("MOCK_UNAUTHORIZED")
            assertThat(errorResponseBody.message).isEqualTo("Mocked Error Message")
            assertThat(errorResponseBody.status).isEqualTo(401)
        }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When creating ErrorResponseBody with no message and 404 status - Then ErrorResponseBody contains fields using element and status`() =
        runTest {
            val outputErrorResponseBody =
                ErrorResponseBody(HttpStatus.NOT_FOUND, "mocked error", "Mock", null)

            assertThat(outputErrorResponseBody.error).isEqualTo("mocked error")
            assertThat(outputErrorResponseBody.errorCode).isEqualTo("MOCK_NOT_FOUND")
            assertThat(outputErrorResponseBody.message).isEqualTo("Mock not found")
            assertThat(outputErrorResponseBody.status).isEqualTo(404)
        }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When creating ErrorResponseBody with no message and 400 status - Then ErrorResponseBody contains fields using element and status`() =
        runTest {
            val outputErrorResponseBody =
                ErrorResponseBody(HttpStatus.BAD_REQUEST, "mocked error", "Mock element", null)

            assertThat(outputErrorResponseBody.error).isEqualTo("mocked error")
            assertThat(outputErrorResponseBody.errorCode).isEqualTo("MOCK_ELEMENT_INVALID")
            assertThat(outputErrorResponseBody.message).isEqualTo("Mock element is invalid")
            assertThat(outputErrorResponseBody.status).isEqualTo(400)
        }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When creating ErrorResponseBody with no message and 401 status - Then ErrorResponseBody contains fields using element and status`() =
        runTest {
            val outputErrorResponseBody =
                ErrorResponseBody(HttpStatus.UNAUTHORIZED, "mocked error", "Mock element", null)

            assertThat(outputErrorResponseBody.error).isEqualTo("mocked error")
            assertThat(outputErrorResponseBody.errorCode).isEqualTo("MOCK_ELEMENT_UNAUTHORIZED")
            assertThat(outputErrorResponseBody.message).isEqualTo("Mock element is not authorized")
            assertThat(outputErrorResponseBody.status).isEqualTo(401)
        }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When creating ErrorResponseBody with no just status - Then ErrorResponseBody contains standard fields and param status`() =
        runTest {
            val outputErrorResponseBody =
                ErrorResponseBody(HttpStatus.CONFLICT)

            assertThat(outputErrorResponseBody.error).isEmpty()
            assertThat(outputErrorResponseBody.errorCode).isEqualTo("ELEMENT_ERROR")
            assertThat(outputErrorResponseBody.message).isEqualTo("error with ELEMENT")
            assertThat(outputErrorResponseBody.status).isEqualTo(HttpStatus.CONFLICT.value())
        }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When creating ErrorResponseBody with no params - Then ErrorResponseBody contains standard fields and param status`() =
        runTest {
            val outputErrorResponseBody =
                ErrorResponseBody()

            assertThat(outputErrorResponseBody.error).isEmpty()
            assertThat(outputErrorResponseBody.errorCode).isEqualTo("ELEMENT_ERROR")
            assertThat(outputErrorResponseBody.message).isEqualTo("error with ELEMENT")
            assertThat(outputErrorResponseBody.status).isEqualTo(500)
        }

    private fun fetchBodyAsErrorResponseBody(serverResponse: ServerResponse): ErrorResponseBody {
        val defaultContext: ServerResponse.Context = object : ServerResponse.Context {
            override fun messageWriters(): List<HttpMessageWriter<*>> {
                return HandlerStrategies.withDefaults().messageWriters()
            }

            override fun viewResolvers(): List<ViewResolver> {
                return Collections.emptyList()
            }
        }

        val request = MockServerHttpRequest.get("http://thisdoenstmatter.com").build()
        val exchange = MockServerWebExchange.from(request)
        serverResponse.writeTo(exchange, defaultContext).block()
        val response = exchange.response
        val responseBodyString = response.bodyAsString.block()!!
        return objectMapper.readValue(responseBodyString, ErrorResponseBody::class.java)
    }
}
