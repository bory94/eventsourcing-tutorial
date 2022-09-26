package com.bory.eventsourcingtutorial.employee.application.event

import com.bory.eventsourcingtutorial.employee.domain.Employee

data class EmployeeUpdatedEvent(
    val employee: Employee
)
