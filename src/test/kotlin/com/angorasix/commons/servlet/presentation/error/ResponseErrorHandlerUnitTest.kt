package com.angorasix.commons.servlet.presentation.error

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.web.servlet.function.ServerResponse
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
    fun `Given IllegalArgumentException - When resolveExceptionResponse - Then response is 400`() =
        runTest {
            val outputResponse =
                resolveExceptionResponse(IllegalArgumentException("Mocked Error Message"))

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST)

            val problem = fetchBodyAsProblem(outputResponse)

            assertThat(problem.title).isEqualTo("IllegalArgumentException")
            assertThat(problem.detail).isEqualTo("Mocked Error Message")
            assertThat(problem.status).isEqualTo(HttpStatus.BAD_REQUEST)
        }

    @Test
    @Throws(Exception::class)
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
            assertThat(problem.detail).isEqualTo("Mocked Error Message")
            assertThat(problem.status).isEqualTo(HttpStatus.BAD_REQUEST)
        }

    @Test
    @Throws(Exception::class)
    fun `Given Exception - When resolveExceptionResponse defining Element - Then response is 500 with customized fields`() =
        runTest {
            val outputResponse =
                resolveExceptionResponse(Exception("Mocked Error Message"), "Mock")

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)

            val problem = fetchBodyAsProblem(outputResponse)

            assertThat(problem.title).isEqualTo("Exception")
            assertThat(problem.detail).isEqualTo("Mocked Error Message")
            assertThat(problem.status).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        }

    @Test
    @Throws(Exception::class)
    fun `When resolveNotFound - Then response is 404 with standard fields`() = runTest {
        val outputResponse = resolveNotFound()

        assertThat(outputResponse.statusCode())
            .isEqualTo(HttpStatus.NOT_FOUND)

        val problem = fetchBodyAsProblem(outputResponse)

        assertThat(problem.title).isEqualTo("ElementNotFound")
        assertThat(problem.detail).isEqualTo("Element not found")
        assertThat(problem.status).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    @Throws(Exception::class)
    fun `When resolveNotFound defining Element and message - Then response is 404 with customized fields`() =
        runTest {
            val outputResponse = resolveNotFound("Mocked Error Message", "Mock")

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.NOT_FOUND)

            val problem = fetchBodyAsProblem(outputResponse)

            assertThat(problem.title).isEqualTo("ElementNotFound")
            assertThat(problem.detail).isEqualTo("Mocked Error Message")
            assertThat(problem.status).isEqualTo(HttpStatus.NOT_FOUND)
        }

    @Test
    @Throws(Exception::class)
    fun `When resolveBadRequest - Then response is 400 with standard fields`() = runTest {
        val outputResponse = resolveBadRequest()

        assertThat(outputResponse.statusCode())
            .isEqualTo(HttpStatus.BAD_REQUEST)

        val problem = fetchBodyAsProblem(outputResponse)

        assertThat(problem.title).isEqualTo("ElementInvalid")
        assertThat(problem.detail).isEqualTo("Element is invalid")
        assertThat(problem.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Throws(Exception::class)
    fun `When resolveBadRequest defining Element and message - Then response is 400 with customized fields`() =
        runTest {
            val outputResponse = resolveBadRequest("Mocked Error Message", "Mock")

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST)

            val problem = fetchBodyAsProblem(outputResponse)

            assertThat(problem.title).isEqualTo("ElementInvalid")
            assertThat(problem.detail).isEqualTo("Mocked Error Message")
            assertThat(problem.status).isEqualTo(HttpStatus.BAD_REQUEST)
        }

    @Test
    @Throws(Exception::class)
    fun `When resolveUnauthorized - Then response is 401 with standard fields`() = runTest {
        val outputResponse = resolveUnauthorized()

        assertThat(outputResponse.statusCode())
            .isEqualTo(HttpStatus.UNAUTHORIZED)

        val problem = fetchBodyAsProblem(outputResponse)

        assertThat(problem.title).isEqualTo("UnauthorizedDueToElement")
        assertThat(problem.detail).isEqualTo("Element is invalid")
        assertThat(problem.status).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    @Throws(Exception::class)
    fun `When resolveUnauthorized defining Element and message - Then response is 401 with customized fields`() =
        runTest {
            val outputResponse = resolveUnauthorized("Mocked Error Message", "Mock")

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED)

            val problem = fetchBodyAsProblem(outputResponse)

            assertThat(problem.title).isEqualTo("UnauthorizedDueToElement")
            assertThat(problem.detail).isEqualTo("Mocked Error Message")
            assertThat(problem.status).isEqualTo(HttpStatus.UNAUTHORIZED)
        }

    private fun fetchBodyAsProblem(serverResponse: ServerResponse): Problem {
        val request = MockHttpServletRequest("GET", "http://thisdoenstmatter.com")
        val context = object : ServerResponse.Context {
            override fun messageConverters(): MutableList<HttpMessageConverter<*>> {
                return mutableListOf(MappingJackson2HttpMessageConverter())
            }
        }
        val response = MockHttpServletResponse()
        serverResponse.writeTo(request, response, context)
        val responseBodyString = response.contentAsString
        val problem =
            objectMapper.readValue(responseBodyString, Problem.ExtendedProblem::class.java)
        var properties = problem.properties as Map<String, Any>?
        return problem.withStatus(HttpStatus.valueOf(properties?.get("status") as Int))
    }
}
