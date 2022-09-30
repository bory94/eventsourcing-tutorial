package com.bory.eventsourcingtutorial.employee.domain

import com.bory.eventsourcingtutorial.employee.application.enum.ProjectAssignStatus
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("employee_project")
class EmployeeProject(
    @Id
    var uuid: String = UUID.randomUUID().toString(),
    var projectUuid: String,
    var status: ProjectAssignStatus = ProjectAssignStatus.ASSIGNING_ACCEPTED,
    var createdAt: Instant = Instant.now()
)