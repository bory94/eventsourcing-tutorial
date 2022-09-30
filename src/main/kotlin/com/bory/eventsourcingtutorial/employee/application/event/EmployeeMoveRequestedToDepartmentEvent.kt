package com.bory.eventsourcingtutorial.employee.application.event

data class EmployeeMoveRequestedToDepartmentEvent(
    val employeeUuid: String,
    val fromDepartmentUuid: String,
    val toDepartmentUuid: String
)
