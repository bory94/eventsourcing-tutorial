package com.bory.eventsourcingtutorial.department.application.event

import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent

data class EmployeeMoveAcceptedEvent(
    val employeeUuid: String,
    val acceptedDepartmentUuid: String
) : AbstractCustomEvent(aggregateUuid = employeeUuid)
