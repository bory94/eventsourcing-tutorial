package com.bory.eventsourcingtutorial.client.infrastructure.persistence

import com.bory.eventsourcingtutorial.client.domain.Project
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.PagingAndSortingRepository

interface ProjectRepository : PagingAndSortingRepository<Project, String> {
    @Query("SELECT project.client_uuid from Project project where uuid = :uuid")
    fun findClientUuidByProjectUuid(uuid: String): String?
}