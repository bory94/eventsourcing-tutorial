package com.bory.eventsourcingtutorial.employee.application.event

data class EmployeeAssignedToProjectEvent(
    val employeeUuid: String,
    val projectUuid: String
)
