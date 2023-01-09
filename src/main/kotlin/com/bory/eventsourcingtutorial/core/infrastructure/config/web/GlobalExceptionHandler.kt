package com.bory.eventsourcingtutorial.core.infrastructure.config.web

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handleConstraintViolationException(
        exception: ConstraintViolationException,
        webRequest: ServletWebRequest
    ) {
        webRequest.response!!.sendError(HttpStatus.BAD_REQUEST.value(), exception.message)
    }
}