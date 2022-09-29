package com.bory.eventsourcingtutorial.employee.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("employee_project")
class EmployeeProject(
    @Id
    var uuid: String = UUID.randomUUID().toString(),
    var projectUuid: String,
    var createdAt: Instant = Instant.now()
)