package com.bory.eventsourcingtutorial.department.application.event

import com.bory.eventsourcingtutorial.department.domain.Department

data class DepartmentCreatedEvent(
    val department: Department
)
