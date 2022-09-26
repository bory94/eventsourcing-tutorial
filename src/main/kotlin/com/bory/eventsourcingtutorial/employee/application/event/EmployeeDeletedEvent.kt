package com.bory.eventsourcingtutorial.employee.application.event

data class EmployeeDeletedEvent(
    val employeeUuid: String
)
