package com.bory.eventsourcingtutorial.employee.application.enum

enum class ProjectAssignStatus {
    BEFORE_REQUEST,
    ASSIGNING_REQUESTED, ASSIGNING_ACCEPTED, ASSIGNING_FAILED,
    UNASSIGNING_REQUESTED, UNASSIGNING_ACCEPTED, UNASSIGNING_FAILED
}