package com.bory.eventsourcingtutorial.employee.application.event

data class EmployeeUnassignRequestedFromProjectEvent(
    val employeeUuid: String,
    val projectUuid: String
)
