package com.bory.eventsourcingtutorial.client.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NoSuchClientException(message: String) : RuntimeException(message)