package com.bory.eventsourcingtutorial.department.infrastructure.persistence

import com.bory.eventsourcingtutorial.department.domain.Department
import org.springframework.data.repository.PagingAndSortingRepository

interface DepartmentRepository : PagingAndSortingRepository<Department, String> {
    fun findByUuidAndDeletedIsFalse(uuid: String): Department?
}