package com.bory.eventsourcingtutorial.client.application.dto

import com.bory.eventsourcingtutorial.core.application.dto.NotInCreatingContext
import org.hibernate.validator.constraints.Length
import java.time.Instant
import javax.validation.constraints.NotBlank

data class ProjectDto(
    @field:NotBlank(groups = [NotInCreatingContext::class])
    val uuid: String? = null,

    @field:NotBlank
    @field:Length(min = 2, max = 256)
    val name: String,

    @field:NotBlank
    @field:Length(min = 5, max = 4000)
    val description: String,

    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),

    @field:NotBlank(groups = [NotInCreatingContext::class])
    val clientUuid: String? = null
)