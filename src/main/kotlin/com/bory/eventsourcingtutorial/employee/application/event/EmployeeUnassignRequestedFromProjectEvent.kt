package com.bory.eventsourcingtutorial.employee.application.event

import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent

data class EmployeeUnassignRequestedFromProjectEvent(
    val employeeUuid: String,
    val projectUuid: String
) : AbstractCustomEvent(aggregateUuid = employeeUuid)
