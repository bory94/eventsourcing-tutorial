package com.bory.eventsourcingtutorial.department.application.event

data class DepartmentDeletedEvent(
    val departmentUuid: String
)
