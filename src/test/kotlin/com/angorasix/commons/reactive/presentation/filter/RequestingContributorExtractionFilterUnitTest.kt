package com.angorasix.commons.reactive.presentation.filter

import com.angorasix.commons.domain.SimpleContributor
import com.angorasix.commons.infrastructure.constants.AngoraSixInfrastructure
import com.angorasix.commons.infrastructure.oauth2.constants.A6WellKnownClaims
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.http.HttpStatus
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.buildAndAwait
import java.time.Instant
import java.util.*

/**
 * @author rozagerardo
 */
@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequestingContributorExtractionFilterUnitTest {

    @Test
    @Throws(Exception::class)
    fun `Given request with Jwt principal - When extractRequestingContributor invoked - Then request contains attribute`() =
        runTest {
            val jwt: Jwt =
                Jwt.withTokenValue("tokenValue").expiresAt(Instant.now().plusSeconds(5000))
                    .issuer("http://localhost:9081")
                    .header("alg", "ger")
                    .claim(A6WellKnownClaims.CONTRIBUTOR_ID, "contributorIdValue").build()
            val authentication = JwtAuthenticationToken(jwt)
            val mockedRequest: ServerRequest =
                MockServerRequest.builder().principal(authentication).build()
            val next: suspend (request: ServerRequest) -> ServerResponse = {
                ServerResponse.ok().buildAndAwait()
            }

            val outputResponse =
                extractRequestingContributor(
                    mockedRequest,
                    next,
                )

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.OK)
            assertThat(mockedRequest.attributes()).containsKey(AngoraSixInfrastructure.REQUEST_ATTRIBUTE_CONTRIBUTOR_KEY)
            val requestingContributor =
                mockedRequest.attributes()[AngoraSixInfrastructure.REQUEST_ATTRIBUTE_CONTRIBUTOR_KEY] as SimpleContributor
            assertThat(requestingContributor.contributorId).isEqualTo("contributorIdValue")
            assertThat(requestingContributor.grants).isEmpty()
        }

    @Test
    @Throws(Exception::class)
    fun `Given request with Affected Contributors Headers - When extractAffectedContributors invoked - Then request contains attribute`() =
        runTest {
            val mockedRequest: ServerRequest =
                MockServerRequest.builder().header(
                    AngoraSixInfrastructure.EVENT_AFFECTED_CONTRIBUTOR_IDS_HEADER,
                    "id1,id2,id99",
                ).build()
            val next: suspend (request: ServerRequest) -> ServerResponse = {
                ServerResponse.ok().buildAndAwait()
            }

            val outputResponse =
                extractAffectedContributors(
                    mockedRequest,
                    next,
                )

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.OK)
            assertThat(mockedRequest.attributes()).containsKey(AngoraSixInfrastructure.REQUEST_ATTRIBUTE_AFFECTED_CONTRIBUTORS_KEY)
            val affectedContributors =
                mockedRequest.attributes()[AngoraSixInfrastructure.REQUEST_ATTRIBUTE_AFFECTED_CONTRIBUTORS_KEY] as List<String>
            assertThat(affectedContributors).containsExactlyInAnyOrder("id1", "id2", "id99")
        }

    @Test
    @Throws(Exception::class)
    fun `Given request without Affected Contributors Headers - When extractAffectedContributors invoked but not required - Then Bad Request response retrieved`() =
        runTest {
            val mockedRequest: ServerRequest =
                MockServerRequest.builder().build()
            val next: suspend (request: ServerRequest) -> ServerResponse = {
                ServerResponse.ok().buildAndAwait()
            }

            val outputResponse =
                extractAffectedContributors(
                    mockedRequest,
                    next,
                )

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.OK)
            assertThat(mockedRequest.attributes()).doesNotContainKey(AngoraSixInfrastructure.REQUEST_ATTRIBUTE_AFFECTED_CONTRIBUTORS_KEY)
        }

    @Test
    @Throws(Exception::class)
    fun `Given request without Affected Contributors Headers - When extractAffectedContributors invoked and required - Then Bad Request response retrieved`() =
        runTest {
            val mockedRequest: ServerRequest =
                MockServerRequest.builder().build()
            val next: suspend (request: ServerRequest) -> ServerResponse = {
                ServerResponse.ok().buildAndAwait()
            }

            val outputResponse =
                extractAffectedContributors(
                    mockedRequest,
                    next,
                    true,
                )

            assertThat(outputResponse.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(mockedRequest.attributes()).doesNotContainKey(AngoraSixInfrastructure.REQUEST_ATTRIBUTE_AFFECTED_CONTRIBUTORS_KEY)
        }
}
