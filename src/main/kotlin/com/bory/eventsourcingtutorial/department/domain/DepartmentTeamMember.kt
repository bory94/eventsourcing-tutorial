package com.bory.eventsourcingtutorial.department.domain

import com.bory.eventsourcingtutorial.department.application.dto.DepartmentTeamMemberDto
import org.springframework.data.annotation.Id
import java.time.Instant
import java.util.*

data class DepartmentTeamMember(
    @Id
    val uuid: String = UUID.randomUUID().toString(),
    val employeeUuid: String,
    val createdAt: Instant = Instant.now()
) {
    fun toDto() = DepartmentTeamMemberDto(
        uuid = uuid,
        employeeUuid = employeeUuid,
        createdAt = createdAt
    )
}
