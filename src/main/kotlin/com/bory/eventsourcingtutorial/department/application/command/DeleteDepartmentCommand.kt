package com.bory.eventsourcingtutorial.department.application.command

import org.hibernate.validator.constraints.Length

data class DeleteDepartmentCommand(
    @field:Length(min = 36, max = 36)
    val departmentUuid: String
)
