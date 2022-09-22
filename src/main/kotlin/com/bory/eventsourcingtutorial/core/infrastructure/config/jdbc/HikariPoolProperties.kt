package com.bory.eventsourcingtutorial.core.infrastructure.config.jdbc

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(value = "spring.datasource.hikari")
data class HikariPoolProperties(
    val driverClassName: String,
    val jdbcUrl: String,
    val username: String,
    val password: String,
    val maximumPoolSize: Int,
    val minimumIdle: Int,
    val poolName: String,
    val connectionTimeout: Long,
    val idleTimeout: Long,
    val maxLifetime: Long
)