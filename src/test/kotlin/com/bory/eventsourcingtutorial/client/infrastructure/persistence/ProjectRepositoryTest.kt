package com.bory.eventsourcingtutorial.client.infrastructure.persistence

import com.bory.eventsourcingtutorial.client.domain.Client
import com.bory.eventsourcingtutorial.client.domain.Project
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
@SpringBootTest
class ProjectRepositoryTest {
    @Autowired
    private lateinit var clientRepository: ClientRepository

    @Autowired
    private lateinit var projectRepository: ProjectRepository

    @Test
    fun `select client uuid from project uuid`() {
        val clientUuid = UUID.randomUUID().toString()
        val projectUuid = UUID.randomUUID().toString()
        val client = Client(
            uuid = clientUuid,
            name = "client name",
            phoneNumber = "client phone",
            address = "client address",
            projects = arrayListOf(
                Project(
                    uuid = projectUuid,
                    name = "project name",
                    description = "project description",
                    clientUuid = clientUuid
                )
            )
        )

        clientRepository.save(client)

        val foundClientUuid = projectRepository.findClientUuidByProjectUuid(projectUuid)
        assertEquals(clientUuid, foundClientUuid)
    }
}