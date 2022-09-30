package com.bory.eventsourcingtutorial.employee.application.command

import org.hibernate.validator.constraints.Length

data class RequestMoveEmployeeToDepartmentCommand(
    @field:Length(min = 36, max = 36)
    val employeeUuid: String,
    @field:Length(min = 36, max = 36)
    val toDepartmentUuid: String
)
