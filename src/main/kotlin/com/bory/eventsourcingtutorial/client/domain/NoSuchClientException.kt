package com.bory.eventsourcingtutorial.client.domain

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NoSuchClientException(message: String) : RuntimeException(message)

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NoSuchProjectException(message: String) : RuntimeException(message)