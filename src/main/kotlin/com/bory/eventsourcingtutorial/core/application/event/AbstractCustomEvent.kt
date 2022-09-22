package com.bory.eventsourcingtutorial.core.application.event

import java.time.Instant
import java.util.*

open class AbstractCustomEvent(
    open val uuid: String = UUID.randomUUID().toString(),
    open val emittedAt: Instant = Instant.now()
)