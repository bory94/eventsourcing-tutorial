package com.bory.eventsourcingtutorial.employee.application.event

data class EmployeeMovedToDepartmentEvent(
    val employeeUuid: String,
    val departmentUuid: String
)
