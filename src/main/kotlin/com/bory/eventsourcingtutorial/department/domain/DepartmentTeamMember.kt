package com.bory.eventsourcingtutorial.department.domain

import com.bory.eventsourcingtutorial.department.application.dto.DepartmentTeamMemberDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("department_team_member")
data class DepartmentTeamMember(
    @Id
    var uuid: String = UUID.randomUUID().toString(),
    var departmentUuid: String,
    var employeeUuid: String,
    var createdAt: Instant = Instant.now()
) {
    fun toDto() = DepartmentTeamMemberDto(
        uuid = uuid,
        employeeUuid = employeeUuid,
        createdAt = createdAt
    )
}
