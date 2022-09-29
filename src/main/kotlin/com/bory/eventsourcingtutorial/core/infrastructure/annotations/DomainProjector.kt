package com.bory.eventsourcingtutorial.core.infrastructure.annotations

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Component

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
annotation class DomainProjector(
    @get:AliasFor(annotation = Component::class)
    val value: String = ""
)
