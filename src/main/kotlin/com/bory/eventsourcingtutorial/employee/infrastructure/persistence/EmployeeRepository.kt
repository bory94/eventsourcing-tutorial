package com.bory.eventsourcingtutorial.employee.infrastructure.persistence

import com.bory.eventsourcingtutorial.employee.domain.Employee
import org.springframework.data.repository.PagingAndSortingRepository

interface EmployeeRepository : PagingAndSortingRepository<Employee, String> {
    fun findByUuidAndDeletedIsFalse(uuid: String): Employee?
}