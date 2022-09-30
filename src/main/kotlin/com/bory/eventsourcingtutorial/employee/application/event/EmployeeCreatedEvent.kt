package com.bory.eventsourcingtutorial.employee.application.event

import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent
import com.bory.eventsourcingtutorial.employee.domain.Employee

data class EmployeeCreatedEvent(
    val employee: Employee
) : AbstractCustomEvent(aggregateUuid = employee.uuid)
