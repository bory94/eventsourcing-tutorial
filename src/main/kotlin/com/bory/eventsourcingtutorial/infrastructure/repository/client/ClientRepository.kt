package com.bory.eventsourcingtutorial.infrastructure.repository.client

import com.bory.eventsourcingtutorial.domain.client.Client
import org.springframework.data.repository.PagingAndSortingRepository

interface ClientRepository : PagingAndSortingRepository<Client, String> {
    fun findByUuidAndDeletedIsFalse(uuid: String): Client?
}