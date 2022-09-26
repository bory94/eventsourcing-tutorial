package com.bory.eventsourcingtutorial.employee.application.command

import com.bory.eventsourcingtutorial.employee.application.dto.EmployeeDto
import javax.validation.Valid

data class UpdateEmployeeCommand(
    @field:Valid
    val employeeDto: EmployeeDto
)
