package com.angorasix.commons.servlet.presentation.filter

import com.angorasix.commons.domain.A6MediaTypes
import com.angorasix.commons.domain.DetailedContributor
import com.angorasix.commons.domain.SimpleContributor
import com.angorasix.commons.infrastructure.constants.AngoraSixInfrastructure
import com.angorasix.commons.infrastructure.oauth2.constants.A6WellKnownClaims
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.oauth2.core.oidc.StandardClaimNames
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
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
    fun `Given request with Jwt principal - When extractRequestingContributor invoked - Then response contains attribute`() =
        runTest {
            val jwt: Jwt =
                Jwt.withTokenValue("tokenValue").expiresAt(Instant.now().plusSeconds(5000))
                    .issuer("http://localhost:10100")
                    .header("alg", "ger")
                    .claim(A6WellKnownClaims.CONTRIBUTOR_ID, "contributorIdValue")
                    .claim(StandardClaimNames.GIVEN_NAME, "firstName")
                    .claim(StandardClaimNames.FAMILY_NAME, "lastName")
                    .claim(StandardClaimNames.PICTURE, "http://example.com/image.jpg").build()
            val authentication = JwtAuthenticationToken(jwt)
            val mockedServletRequest = MockHttpServletRequest("PATCH", "/contributors/123")
            mockedServletRequest.userPrincipal = authentication
            val mockedRequest = ServerRequest.create(mockedServletRequest, emptyList())
            val next: (request: ServerRequest) -> ServerResponse = {
                ServerResponse.ok().build()
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
                mockedRequest.attributes()[AngoraSixInfrastructure.REQUEST_ATTRIBUTE_CONTRIBUTOR_KEY] as DetailedContributor
            assertThat(requestingContributor.contributorId).isEqualTo("contributorIdValue")
            assertThat(requestingContributor.grants).isEmpty()
            assertThat(requestingContributor.firstName).isEqualTo("firstName")
            assertThat(requestingContributor.lastName).isEqualTo("lastName")
            assertThat(requestingContributor.profileMedia?.mediaType).isEqualTo(A6MediaTypes.IMAGE.value)
            assertThat(requestingContributor.profileMedia?.url).isEqualTo("http://example.com/image.jpg")
        }

    @Test
    @Throws(Exception::class)
    fun `Given request with Jwt principal only base fields - When extractRequestingContributor invoked - Then request contains attribute`() =
        runTest {
            val jwt: Jwt =
                Jwt.withTokenValue("tokenValue").expiresAt(Instant.now().plusSeconds(5000))
                    .issuer("http://localhost:10100")
                    .header("alg", "ger")
                    .claim(A6WellKnownClaims.CONTRIBUTOR_ID, "contributorIdValue").build()
            val authentication = JwtAuthenticationToken(jwt)
            val mockedServletRequest = MockHttpServletRequest("PATCH", "/contributors/123")
            mockedServletRequest.userPrincipal = authentication
            val mockedRequest = ServerRequest.create(mockedServletRequest, emptyList())
            val next: (request: ServerRequest) -> ServerResponse = {
                ServerResponse.ok().build()
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

            val requestingDetailedContributor =
                mockedRequest.attributes()[AngoraSixInfrastructure.REQUEST_ATTRIBUTE_CONTRIBUTOR_KEY] as DetailedContributor
            assertThat(requestingDetailedContributor.contributorId).isEqualTo("contributorIdValue")
            assertThat(requestingDetailedContributor.grants).isEmpty()
            assertThat(requestingDetailedContributor.firstName).isNull()
            assertThat(requestingDetailedContributor.lastName).isNull()
            assertThat(requestingDetailedContributor.profileMedia).isNull()
        }
}
