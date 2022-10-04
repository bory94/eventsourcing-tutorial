package com.bory.eventsourcingtutorial.client.application.dto

import com.bory.eventsourcingtutorial.core.application.dto.NotInCreatingContext
import org.hibernate.validator.constraints.Length
import java.time.Instant
import javax.validation.Valid
import javax.validation.constraints.NotBlank

data class ClientDto(
    @field:NotBlank(groups = [NotInCreatingContext::class])
    val uuid: String? = null,

    @field:NotBlank
    @field:Length(min = 2, max = 100)
    val name: String,

    @field:NotBlank
    @field:Length(min = 5, max = 20)
    val phoneNumber: String,

    @field:NotBlank
    @field:Length(min = 5, max = 100)
    val address: String,

    val deleted: Boolean = false,
    val version: Int = 0,

    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),

    @field:Valid
    val projects: List<ProjectDto> = listOf()
)
