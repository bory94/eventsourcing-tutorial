package com.bory.eventsourcingtutorial.core.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.validation.ConstraintViolationException
import javax.validation.Validation
import javax.validation.Validator

@Configuration
class ValidatorConfig {
    @Bean
    fun customValidator(): Validator = Validation.buildDefaultValidatorFactory().validator
}

fun Validator.validateAndThrow(
    value: Any,
    vararg groups: Class<*>
) = this.validate(value, *groups).let { violations ->
    if (violations.isNotEmpty()) throw ConstraintViolationException(violations)
}