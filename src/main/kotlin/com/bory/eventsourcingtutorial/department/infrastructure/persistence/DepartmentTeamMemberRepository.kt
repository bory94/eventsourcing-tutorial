package com.bory.eventsourcingtutorial.department.infrastructure.persistence

import com.bory.eventsourcingtutorial.department.domain.DepartmentTeamMember
import org.springframework.data.repository.PagingAndSortingRepository

interface DepartmentTeamMemberRepository :
    PagingAndSortingRepository<DepartmentTeamMember, String> {
    fun deleteByEmployeeUuid(employeeUuid: String)
}