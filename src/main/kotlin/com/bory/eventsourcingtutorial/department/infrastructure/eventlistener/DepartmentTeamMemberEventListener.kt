package com.bory.eventsourcingtutorial.department.infrastructure.eventlistener

import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainEventListener
import com.bory.eventsourcingtutorial.department.domain.exception.NoSuchDepartmentException
import com.bory.eventsourcingtutorial.department.infrastructure.persistence.DepartmentRepository
import com.bory.eventsourcingtutorial.department.infrastructure.persistence.DepartmentTeamMemberRepository
import com.bory.eventsourcingtutorial.employee.application.event.EmployeeMovedToDepartmentEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Transactional

@Transactional
@DomainEventListener
class DepartmentTeamMemberEventListener(
    private val departmentRepository: DepartmentRepository,
    private val departmentTeamMemberRepository: DepartmentTeamMemberRepository
) {
    @Async
    @EventListener(classes = [EmployeeMovedToDepartmentEvent::class])
    fun on(event: EmployeeMovedToDepartmentEvent) {
        departmentTeamMemberRepository.deleteByEmployeeUuid(event.employeeUuid)

        val department = departmentRepository.findByUuidAndDeletedIsFalse(event.departmentUuid)
            ?: throw NoSuchDepartmentException("Department uuid[${event.departmentUuid} not found.")

        department.addTeamMember(event.employeeUuid)
        departmentRepository.save(department)
    }
}