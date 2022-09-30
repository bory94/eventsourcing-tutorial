package com.bory.eventsourcingtutorial.employee.application.event

data class EmployeeAssignRequestedToProjectEvent(
    val employeeUuid: String,
    val projectUuid: String
)
