package com.bory.eventsourcingtutorial.infrastructure.repository.client

import com.bory.eventsourcingtutorial.domain.client.Project
import org.springframework.data.repository.PagingAndSortingRepository

interface ProjectRepository : PagingAndSortingRepository<Project, String>