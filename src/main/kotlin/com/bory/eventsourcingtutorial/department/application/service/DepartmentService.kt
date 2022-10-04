package com.bory.eventsourcingtutorial.department.application.service

import com.bory.eventsourcingtutorial.department.application.command.CreateDepartmentCommand
import com.bory.eventsourcingtutorial.department.application.command.DeleteDepartmentCommand
import com.bory.eventsourcingtutorial.department.application.command.UpdateDepartmentCommand
import com.bory.eventsourcingtutorial.department.domain.Department
import com.bory.eventsourcingtutorial.department.domain.exception.NoSuchDepartmentException
import com.bory.eventsourcingtutorial.department.infrastructure.persistence.DepartmentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class DepartmentService(
    private val departmentRepository: DepartmentRepository
) {
    fun create(command: CreateDepartmentCommand): Department =
        departmentRepository.save(Department(UUID.randomUUID().toString(), command))


    fun update(uuid: String, command: UpdateDepartmentCommand): Department {
        val department = departmentRepository.findByUuidAndDeletedIsFalse(uuid)
            ?: throw NoSuchDepartmentException("Department uuid[$uuid] not found.")

        department.updateWith(Department(uuid, command))

        return departmentRepository.save(department)
    }

    fun delete(command: DeleteDepartmentCommand): Department {
        val department = departmentRepository.findByUuidAndDeletedIsFalse(command.departmentUuid)
            ?: throw NoSuchDepartmentException("Department uuid[${command.departmentUuid}] not found.")

        department.delete()

        return departmentRepository.save(department)
    }
}