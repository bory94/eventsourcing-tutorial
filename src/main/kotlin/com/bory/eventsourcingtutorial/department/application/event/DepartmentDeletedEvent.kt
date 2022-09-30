package com.bory.eventsourcingtutorial.department.application.event

import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent

data class DepartmentDeletedEvent(
    val departmentUuid: String
) : AbstractCustomEvent(aggregateUuid = departmentUuid)
