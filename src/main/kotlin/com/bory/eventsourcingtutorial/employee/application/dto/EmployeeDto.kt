package com.bory.eventsourcingtutorial.employee.application.dto

import com.bory.eventsourcingtutorial.core.application.dto.NotInCreatingContext
import org.hibernate.validator.constraints.Length
import java.time.Instant
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class EmployeeDto(
    @field:NotBlank(groups = [NotInCreatingContext::class])
    val uuid: String? = null,
    @field:NotBlank @field:Length(min = 2, max = 36)
    val name: String,
    @field:Min(1)
    val age: Int,
    @field:Min(1)
    val salary: Int,
    @field:NotBlank @field:Length(min = 2, max = 20)
    val position: String,
    val deleted: Boolean = false,
    val version: Int = 1,
    @field:NotBlank @field:Length(min = 36, max = 36)
    val departmentUuid: String,

    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
)
