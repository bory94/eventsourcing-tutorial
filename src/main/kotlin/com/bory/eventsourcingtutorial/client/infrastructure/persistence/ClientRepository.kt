package com.bory.eventsourcingtutorial.client.infrastructure.persistence

import com.bory.eventsourcingtutorial.client.domain.Client
import org.springframework.data.repository.PagingAndSortingRepository

interface ClientRepository : PagingAndSortingRepository<Client, String> {
    fun findByUuidAndDeletedIsFalse(uuid: String): Client?
}