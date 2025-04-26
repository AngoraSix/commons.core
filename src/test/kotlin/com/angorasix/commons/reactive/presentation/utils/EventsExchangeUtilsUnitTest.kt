package com.angorasix.commons.reactive.presentation.utils

import com.angorasix.commons.infrastructure.constants.AngoraSixInfrastructure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventsExchangeUtilsUnitTest {
    @Test
    @Throws(Exception::class)
    fun `Given request with Triggers Event header - When affectedContributors invoked - Then response contains header`() =
        runTest {
            val response = ServerResponse.ok()
            val mockedRequest: ServerRequest =
                MockServerRequest
                    .builder()
                    .header(
                        AngoraSixInfrastructure.TRIGGERS_EVENT_HEADER,
                        "true",
                    ).build()

            response.affectedContributors(
                mockedRequest,
                listOf("contributorId1", "contributorId99"),
            )

            response.headers {
                assertThat(it.get(AngoraSixInfrastructure.EVENT_AFFECTED_CONTRIBUTOR_IDS_HEADER)).contains(
                    "contributorId1,contributorId99",
                )
            }
        }

    @Test
    @Throws(Exception::class)
    fun `Given request with false Triggers Event header - When affectedContributors invoked - Then response does not contain header`() =
        runTest {
            val response = ServerResponse.ok()
            val mockedRequest: ServerRequest =
                MockServerRequest
                    .builder()
                    .header(
                        AngoraSixInfrastructure.TRIGGERS_EVENT_HEADER,
                        "false",
                    ).build()

            response.affectedContributors(
                mockedRequest,
                listOf("contributorId1", "contributorId99"),
            )

            response.headers { assertThat(it.get(AngoraSixInfrastructure.EVENT_AFFECTED_CONTRIBUTOR_IDS_HEADER)).isNull() }
        }

    @Test
    @Throws(Exception::class)
    fun `Given request with null Triggers Event header - When affectedContributors invoked - Then response does not contain header`() =
        runTest {
            val response = ServerResponse.ok()
            val mockedRequest: ServerRequest = MockServerRequest.builder().build()

            response.affectedContributors(
                mockedRequest,
                listOf("contributorId1", "contributorId99"),
            )

            response.headers { assertThat(it.get(AngoraSixInfrastructure.EVENT_AFFECTED_CONTRIBUTOR_IDS_HEADER)).isNull() }
        }

    @Test
    @Throws(Exception::class)
    fun `Given Triggers Event header - When affectedContributors invoked without contributors - Then response contains empty header`() =
        runTest {
            val response = ServerResponse.ok()
            val mockedRequest: ServerRequest =
                MockServerRequest
                    .builder()
                    .header(
                        AngoraSixInfrastructure.TRIGGERS_EVENT_HEADER,
                        "true",
                    ).build()

            response.affectedContributors(
                mockedRequest,
                emptyList(),
            )

            response.headers {
                assertThat(it.get(AngoraSixInfrastructure.EVENT_AFFECTED_CONTRIBUTOR_IDS_HEADER)).contains(
                    "",
                )
            }
        }
}
