package com.bory.eventsourcingtutorial.employee.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NoSuchEmployeeException(message: String) : RuntimeException(message)