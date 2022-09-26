package com.bory.eventsourcingtutorial.employee.application.event

data class EmployeeUnassignedFromProjectEvent(
    val employeeUuid: String,
    val projectUuid: String
)
