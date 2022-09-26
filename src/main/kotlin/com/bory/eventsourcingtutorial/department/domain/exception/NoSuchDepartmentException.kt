package com.bory.eventsourcingtutorial.department.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NoSuchDepartmentException(message: String) : RuntimeException(message)