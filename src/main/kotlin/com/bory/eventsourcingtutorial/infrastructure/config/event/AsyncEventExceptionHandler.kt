package com.bory.eventsourcingtutorial.infrastructure.config.event

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component
class AsyncEventExceptionHandler : AsyncUncaughtExceptionHandler {
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(AsyncEventExceptionHandler::class.java)
    }

    override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any?) {
        LOGGER.error("Async Exception Occurred!: $method ::: $params", ex)
    }
}