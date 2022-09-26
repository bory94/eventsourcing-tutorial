package com.bory.eventsourcingtutorial.department.application.dto

import com.bory.eventsourcingtutorial.core.application.dto.NotInCreatingContext
import org.hibernate.validator.constraints.Length
import java.time.Instant
import javax.validation.constraints.NotBlank

data class DepartmentDto(
    @field:NotBlank(groups = [NotInCreatingContext::class])
    val uuid: String? = null,
    @field:NotBlank @field:Length(min = 2, max = 36)
    val name: String,
    @field:NotBlank @field:Length(max = 4000)
    val description: String,

    val deleted: Boolean = false,
    val version: Int = 1,

    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)
