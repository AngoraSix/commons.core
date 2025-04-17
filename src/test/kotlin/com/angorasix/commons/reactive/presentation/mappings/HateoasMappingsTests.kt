package com.angorasix.commons.reactive.presentation.mappings

import com.angorasix.commons.infrastructure.config.configurationproperty.api.Route
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.argumentCaptor
import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel
import org.springframework.http.HttpMethod
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.web.reactive.function.server.ServerRequest

class HateoasMappingsTests {
    @Test
    fun `Given Route and RepresentationModel - when addSelfLink - then Link generatedCorrectly`() =
        runTest {
            val mockedExchange =
                MockServerWebExchange.from(
                    MockServerHttpRequest.get("/base/url").build(),
                )
            val mockedRequest: ServerRequest =
                MockServerRequest.builder().exchange(mockedExchange).build()
            val representationModel = mock(RepresentationModel::class.java)
            val route = Route("mockedRouteName", basePaths = emptyList(), HttpMethod.GET, "/mockedPath/{param}")

            representationModel.addSelfLink(route, mockedRequest, listOf("expandArg"))

            val linkCaptor = argumentCaptor<Link>()

            verify(representationModel).add(linkCaptor.capture())
            val capturedLink = linkCaptor.firstValue

            // 🔍 Then you can assert properties on it
            Assertions.assertThat(capturedLink.rel.value()).isEqualTo("self")
            Assertions.assertThat(capturedLink.href).contains("/mockedPath/expandArg")
        }

    @Test
    fun `Given Route and RepresentationModel - when addLink without Input - then Link generatedCorrectly`() =
        runTest {
            val mockedExchange =
                MockServerWebExchange.from(
                    MockServerHttpRequest.get("/base/url").build(),
                )
            val mockedRequest: ServerRequest =
                MockServerRequest.builder().exchange(mockedExchange).build()
            val representationModel = mock(RepresentationModel::class.java)
            val route = Route("mockedRouteName", basePaths = emptyList(), HttpMethod.GET, "/mockedPath/{param}")

            representationModel.addLink(route, "actionName", mockedRequest, listOf("expandArg"))

            val linkCaptor = argumentCaptor<Link>()

            verify(representationModel).add(linkCaptor.capture())
            val capturedLink = linkCaptor.firstValue

            // 🔍 Then you can assert properties on it
            Assertions.assertThat(capturedLink.rel.value()).isEqualTo("actionName")
            Assertions.assertThat(capturedLink.href).contains("/mockedPath/expandArg")
            Assertions
                .assertThat(
                    capturedLink.affordances
                        .first()
                        .first()
                        .input.type,
                ).isNull()
        }

    @Test
    fun `Given Route and RepresentationModel - when addLink - then Link generatedCorrectly`() =
        runTest {
            val mockedExchange =
                MockServerWebExchange.from(
                    MockServerHttpRequest.get("/base/url").build(),
                )
            val mockedRequest: ServerRequest =
                MockServerRequest.builder().exchange(mockedExchange).build()
            val representationModel = mock(RepresentationModel::class.java)
            val route = Route("mockedRouteName", basePaths = emptyList(), HttpMethod.GET, "/mockedPath/{param}")

            representationModel.addLink(route, "actionName", mockedRequest, listOf("expandArg"), Long::class.java)

            val linkCaptor = argumentCaptor<Link>()

            verify(representationModel).add(linkCaptor.capture())
            val capturedLink = linkCaptor.firstValue

            // 🔍 Then you can assert properties on it
            Assertions.assertThat(capturedLink.rel.value()).isEqualTo("actionName")
            Assertions.assertThat(capturedLink.href).contains("/mockedPath/expandArg")
            Assertions
                .assertThat(
                    capturedLink.affordances
                        .first()
                        .first()
                        .input.type,
                ).isEqualTo(Long::class.java)
        }
}
