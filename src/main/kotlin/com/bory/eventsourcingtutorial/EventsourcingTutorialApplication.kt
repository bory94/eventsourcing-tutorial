package com.bory.eventsourcingtutorial

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties
class EventsourcingTutorialApplication

fun main(args: Array<String>) {
    runApplication<EventsourcingTutorialApplication>(*args)
}
