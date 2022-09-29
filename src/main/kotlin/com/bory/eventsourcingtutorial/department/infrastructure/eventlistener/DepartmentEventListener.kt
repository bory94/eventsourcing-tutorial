package com.bory.eventsourcingtutorial.department.infrastructure.eventlistener

import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainEventListener
import com.bory.eventsourcingtutorial.core.infrastructure.extensions.retry
import com.bory.eventsourcingtutorial.department.application.event.DepartmentCreatedEvent
import com.bory.eventsourcingtutorial.department.application.event.DepartmentDeletedEvent
import com.bory.eventsourcingtutorial.department.application.event.DepartmentUpdatedEvent
import com.bory.eventsourcingtutorial.department.domain.exception.NoSuchDepartmentException
import com.bory.eventsourcingtutorial.department.infrastructure.persistence.DepartmentRepository
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Transactional

@Transactional
@DomainEventListener
class DepartmentEventListener(
    private val departmentRepository: DepartmentRepository
) {
    @Async
    @EventListener(classes = [DepartmentCreatedEvent::class])
    fun on(event: DepartmentCreatedEvent) {
        retry {
            departmentRepository.save(event.department)
        }
    }

    @Async
    @EventListener(classes = [DepartmentUpdatedEvent::class])
    fun on(event: DepartmentUpdatedEvent) {
        retry(skipRetryExceptions = arrayOf(NoSuchDepartmentException::class.java)) {
            val department = departmentRepository.findByUuidAndDeletedIsFalse(event.department.uuid)
                ?: throw NoSuchDepartmentException("Department uuid[${event.department.uuid}] not found.")

            department.updateWith(event.department)

            departmentRepository.save(department)
        }
    }

    @Async
    @EventListener(classes = [DepartmentDeletedEvent::class])
    fun on(event: DepartmentDeletedEvent) {
        retry(skipRetryExceptions = arrayOf(NoSuchDepartmentException::class.java)) {
            val department = departmentRepository.findByUuidAndDeletedIsFalse(event.departmentUuid)
                ?: throw NoSuchDepartmentException("Department uuid[${event.departmentUuid}] not found.")

            department.delete()

            departmentRepository.save(department)
        }
    }
}