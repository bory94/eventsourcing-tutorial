package com.bory.eventsourcingtutorial.department.application.event

import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent

data class EmployeeReleasedEvent(
    val employeeUuid: String,
    val releasedDepartmentUuid: String
) : AbstractCustomEvent(aggregateUuid = employeeUuid)
