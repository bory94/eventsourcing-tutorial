package com.bory.eventsourcingtutorial.employee.application.enum

enum class EmployeePosition(val title: String) {
    INTERN("인턴"), STAFF("사원"), ASSISTANT_MANAGER("대리"),
    MANAGER("과장"), GENERAL_MANAGER("차장"), VICE_PRESIDENT("부장"),
    SENIOR_PRESIDENT("부사장"), PRESIDENT("사장")
}