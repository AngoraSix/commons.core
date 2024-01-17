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
            val addMember = A6InfraTopics.ADD_MEMBER
            val removeMember = A6InfraTopics.REMOVE_MEMBER

            val addMemberValue = addMember.value
            val removeMemberValue = removeMember.value

            assertThat(addMemberValue).isEqualTo("addMember")
            assertThat(removeMemberValue).isEqualTo("removeMember")
        }

    @Test
    @Throws(Exception::class)
    fun `Given A6DomainResource - When getValue - Then corresponding value is retrieved`() =
        runTest {
            val clubResource = A6DomainResource.CLUB
            val contributorResource = A6DomainResource.CONTRIBUTOR

            val clubResourceValue = clubResource.value
            val contributorResourceValue = contributorResource.value

            assertThat(clubResourceValue).isEqualTo("club")
            assertThat(contributorResourceValue).isEqualTo("contributor")
        }
}
