package com.bory.eventsourcingtutorial.department.application.event

import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent
import com.bory.eventsourcingtutorial.employee.application.event.EmployeeMoveRequestedToDepartmentEvent

data class EmployeeMoveFailedEvent(
    val employeeUuid: String,
    val fromDepartmentUuid: String?,
    val toDepartmentUuid: String,
    val reason: String
) : AbstractCustomEvent(aggregateUuid = employeeUuid) {
    constructor(event: EmployeeMoveRequestedToDepartmentEvent, reason: String) : this(
        event.employeeUuid, event.fromDepartmentUuid, event.toDepartmentUuid, reason
    )
}
