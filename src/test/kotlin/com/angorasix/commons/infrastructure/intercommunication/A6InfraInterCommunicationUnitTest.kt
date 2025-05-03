package com.angorasix.commons.infrastructure.intercommunication

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
class A6InfraInterCommunicationUnitTest {
    @Test
    @Throws(Exception::class)
    fun `Given A6InfraTopic - When getValue - Then corresponding value is retrieved`() =
        runTest {
            val projectMemberJoined = A6InfraTopics.PROJECT_CLUB_MEMBER_JOINED
            val managementMemberJoined = A6InfraTopics.MANAGEMENT_CLUB_MEMBER_JOINED

            val projectMemberJoinedValue = projectMemberJoined.value
            val managementMemberJoinedValue = managementMemberJoined.value

            assertThat(projectMemberJoinedValue).isEqualTo("projectClubMemberJoined")
            assertThat(managementMemberJoinedValue).isEqualTo("managementClubMemberJoined")
        }

    @Test
    @Throws(Exception::class)
    fun `Given A6DomainResource - When getValue - Then corresponding value is retrieved`() =
        runTest {
            val clubResource = A6DomainResource.CLUB

            val clubResourceValue = clubResource.value

            assertThat(clubResourceValue).isEqualTo("club")
        }
}
