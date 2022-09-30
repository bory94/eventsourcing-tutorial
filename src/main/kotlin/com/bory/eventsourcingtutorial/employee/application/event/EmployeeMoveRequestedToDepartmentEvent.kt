package com.bory.eventsourcingtutorial.employee.application.event

import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent

data class EmployeeMoveRequestedToDepartmentEvent(
    val employeeUuid: String,
    val fromDepartmentUuid: String,
    val toDepartmentUuid: String
) : AbstractCustomEvent(aggregateUuid = employeeUuid)
