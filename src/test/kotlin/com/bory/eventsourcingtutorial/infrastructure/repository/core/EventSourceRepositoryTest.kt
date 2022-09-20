package com.bory.eventsourcingtutorial.infrastructure.repository.core

import com.bory.eventsourcingtutorial.domain.core.EventSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
@SpringBootTest
class EventSourceRepositoryTest {
    @Autowired
    private lateinit var eventSourceRepository: EventSourceRepository

    @Test
    fun `insert event source entity`() {
        val es = EventSource(
            type = "x.y.ClientCreatedEvent",
            aggregateId = UUID.randomUUID().toString(),
            payload = """{"Hello":"World"}"""
        )

        println(es)
        println("--------------------------------------")

        val saved = eventSourceRepository.save(es)

        println(saved)

        val found = eventSourceRepository.findById(saved.uuid).orElseThrow()

        println("--------------------------------------")
        println(found)

        println("--------------------------------------")
        val newlySaved = eventSourceRepository.save(found)

        assertEquals(newlySaved.uuid, saved.uuid)
    }
}