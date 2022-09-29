package com.bory.eventsourcingtutorial.client.application.dto

import com.bory.eventsourcingtutorial.core.application.dto.NotInCreatingContext
import org.hibernate.validator.constraints.Length
import java.time.Instant
import javax.validation.constraints.NotBlank

data class ProjectAssigneeDto(
    @field:NotBlank(groups = [NotInCreatingContext::class])
    val uuid: String? = null,
    @field:Length(min = 36, max = 36)
    val projectUuid: String,
    @field:Length(min = 36, max = 36)
    val employeeUuid: String,
    val createdAt: Instant = Instant.now()
)
