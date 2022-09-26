package com.bory.eventsourcingtutorial.employee.domain

import com.bory.eventsourcingtutorial.employee.infrastructure.persistence.EmployeeRepository
import com.fasterxml.jackson.databind.ObjectMapper

class EmployeeProjector(
    private val employeeRepository: EmployeeRepository,
    private val objectMapper: ObjectMapper
) {
}