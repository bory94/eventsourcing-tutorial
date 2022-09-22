package com.bory.eventsourcingtutorial.core.infrastructure.config.jdbc

import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableJdbcAuditing
@EnableTransactionManagement
class DatasourceConfig(
    private val hikariPoolProperties: HikariPoolProperties
) {
    @Bean
    fun dataSource(): HikariDataSource {
        val dataSource = HikariDataSource()

        with(hikariPoolProperties) {
            dataSource.driverClassName = driverClassName
            dataSource.jdbcUrl = jdbcUrl
            dataSource.username = username
            dataSource.password = password
            dataSource.maximumPoolSize = maximumPoolSize
            dataSource.minimumIdle = minimumIdle
            dataSource.poolName = poolName
            dataSource.connectionTimeout = connectionTimeout
            dataSource.idleTimeout = idleTimeout
            dataSource.maxLifetime = maxLifetime
        }

        return dataSource

    }
}