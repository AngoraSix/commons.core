package com.angorasix.commons.reactive.presentation.error

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.hateoas.mediatype.problem.Problem
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

            val problem = fetchBodyAsProblem(outputResponse)

            assertThat(problem.title).isEqualTo("IllegalArgumentException")
//            assertThat(problem).isEqualTo("ELEMENT_INVALID")
            assertThat(problem.detail).isEqualTo("Mocked Error Message")
            assertThat(problem.status).isEqualTo(HttpStatus.BAD_REQUEST)
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

            val problem = fetchBodyAsProblem(outputResponse)

            assertThat(problem.title).isEqualTo("IllegalArgumentException")
//            assertThat(problem.errorCode).isEqualTo("MOCK_INVALID")
            assertThat(problem.detail).isEqualTo("Mocked Error Message")
            assertThat(problem.status).isEqualTo(HttpStatus.BAD_REQUEST)
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

            val problem = fetchBodyAsProblem(outputResponse)

            assertThat(problem.title).isEqualTo("Exception")
//            assertThat(problem.errorCode).isEqualTo("MOCK_ERROR")
            assertThat(problem.detail).isEqualTo("Mocked Error Message")
            assertThat(problem.status).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When resolveNotFound - Then response is 404 with standard fields`() = runTest {
        val outputResponse = resolveNotFound()

        assertThat(outputResponse.statusCode())
            .isEqualTo(HttpStatus.NOT_FOUND)

        val problem = fetchBodyAsProblem(outputResponse)

        assertThat(problem.title).isEqualTo("ElementNotFound")
//        assertThat(problem.errorCode).isEqualTo("ELEMENT_NOT_FOUND")
        assertThat(problem.detail).isEqualTo("Element not found")
        assertThat(problem.status).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When resolveNotFound defining Element and message - Then response is 404 with customized fields`() =
        runTest {
            val outputResponse = resolveNotFound("Mocked Error Message", "Mock")

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.NOT_FOUND)

            val problem = fetchBodyAsProblem(outputResponse)

            assertThat(problem.title).isEqualTo("ElementNotFound")
//            assertThat(problem.errorCode).isEqualTo("MOCK_NOT_FOUND")
            assertThat(problem.detail).isEqualTo("Mocked Error Message")
            assertThat(problem.status).isEqualTo(HttpStatus.NOT_FOUND)
        }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When resolveBadRequest - Then response is 400 with standard fields`() = runTest {
        val outputResponse = resolveBadRequest()

        assertThat(outputResponse.statusCode())
            .isEqualTo(HttpStatus.BAD_REQUEST)

        val problem = fetchBodyAsProblem(outputResponse)

        assertThat(problem.title).isEqualTo("ElementInvalid")
//        assertThat(problem.errorCode).isEqualTo("ELEMENT_INVALID")
        assertThat(problem.detail).isEqualTo("Element is invalid")
        assertThat(problem.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When resolveBadRequest defining Element and message - Then response is 400 with customized fields`() =
        runTest {
            val outputResponse = resolveBadRequest("Mocked Error Message", "Mock")

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST)

            val problem = fetchBodyAsProblem(outputResponse)

            assertThat(problem.title).isEqualTo("ElementInvalid")
//            assertThat(problem.errorCode).isEqualTo("MOCK_INVALID")
            assertThat(problem.detail).isEqualTo("Mocked Error Message")
            assertThat(problem.status).isEqualTo(HttpStatus.BAD_REQUEST)
        }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When resolveUnauthorized - Then response is 401 with standard fields`() = runTest {
        val outputResponse = resolveUnauthorized()

        assertThat(outputResponse.statusCode())
            .isEqualTo(HttpStatus.UNAUTHORIZED)

        val problem = fetchBodyAsProblem(outputResponse)

        assertThat(problem.title).isEqualTo("UnauthorizedDueToElement")
//        assertThat(problem.errorCode).isEqualTo("ELEMENT_UNAUTHORIZED")
        assertThat(problem.detail).isEqualTo("Element is invalid")
        assertThat(problem.status).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    @Throws(Exception::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `When resolveUnauthorized defining Element and message - Then response is 401 with customized fields`() =
        runTest {
            val outputResponse = resolveUnauthorized("Mocked Error Message", "Mock")

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED)

            val problem = fetchBodyAsProblem(outputResponse)

            assertThat(problem.title).isEqualTo("UnauthorizedDueToElement")
//            assertThat(problem.errorCode).isEqualTo("MOCK_UNAUTHORIZED")
            assertThat(problem.detail).isEqualTo("Mocked Error Message")
            assertThat(problem.status).isEqualTo(HttpStatus.UNAUTHORIZED)
        }

    private fun fetchBodyAsProblem(serverResponse: ServerResponse): Problem {
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
        val problem =
            objectMapper.readValue(responseBodyString, Problem.ExtendedProblem::class.java)
        var properties = problem.properties as Map<String, Any>?
        return problem.withStatus(HttpStatus.valueOf(properties?.get("status") as Int))
    }
}
