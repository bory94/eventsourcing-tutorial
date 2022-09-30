package com.bory.eventsourcingtutorial.department.application.command

import com.bory.eventsourcingtutorial.department.application.dto.DepartmentDto
import javax.validation.Valid

data class CreateDepartmentCommand(
    @field:Valid
    val department: DepartmentDto
)
