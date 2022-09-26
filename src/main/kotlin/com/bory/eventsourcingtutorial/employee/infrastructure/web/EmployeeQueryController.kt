package com.bory.eventsourcingtutorial.employee.infrastructure.web

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Transactional(readOnly = true)
@RequestMapping("/api/v1/employees")
class EmployeeQueryController {
}