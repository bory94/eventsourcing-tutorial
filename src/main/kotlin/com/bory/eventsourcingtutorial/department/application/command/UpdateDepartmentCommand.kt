package com.bory.eventsourcingtutorial.department.application.command

import com.bory.eventsourcingtutorial.department.application.dto.DepartmentDto
import javax.validation.Valid

data class UpdateDepartmentCommand(
    @field:Valid
    val departmentDto: DepartmentDto
)
