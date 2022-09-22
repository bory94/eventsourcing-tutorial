package com.bory.eventsourcingtutorial.core.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@EnableAsync
@Configuration
class EventExecutorConfig {
    @Bean
    @Primary
    fun eventThreadPoolExecutor() = ThreadPoolTaskExecutor().apply {
        corePoolSize = 16
        maxPoolSize = 32
        initialize()
    }
}