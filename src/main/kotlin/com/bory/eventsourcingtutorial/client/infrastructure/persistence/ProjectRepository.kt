package com.bory.eventsourcingtutorial.client.infrastructure.persistence

import com.bory.eventsourcingtutorial.client.domain.Project
import org.springframework.data.repository.PagingAndSortingRepository

interface ProjectRepository : PagingAndSortingRepository<Project, String>