package com.bory.eventsourcingtutorial.core.infrastructure.annotations

import org.springframework.core.annotation.AliasFor
import org.springframework.web.bind.annotation.RestController

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@RestController
annotation class QueryController(
    @get:AliasFor(annotation = RestController::class)
    val value: String = ""
)
