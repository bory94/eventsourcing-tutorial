package com.bory.eventsourcingtutorial.employee.application.event

import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent

data class EmployeeDeletedEvent(
    val employeeUuid: String
) : AbstractCustomEvent(aggregateUuid = employeeUuid)
