package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppealServiceImpl (
        private val userCrudRepository: UserCrudRepository,
        private val appealCrudRepository: AppealCrudRepository,
        private val appealTypeCrudRepository: AppealTypeCrudRepository,
        private val appealStatusCrudRepository: AppealStatusCrudRepository
) : AppealService {
    override fun get(id: Long): Appeal {
        val appealOptional = appealCrudRepository.findById(id)
        if (appealOptional.isEmpty) {
            throw NotFoundException("Appeal not found")
        }
        return appealOptional.get()
    }

    override fun all(): List<Appeal> {
        return appealCrudRepository.findAll()
    }

    override fun own(): List<Appeal> {
        val user = userCrudRepository.findByEmail("student@edu.hse.ru").get()
        return appealCrudRepository.findAllByAuthor(user)
    }

    override fun feed(): List<Appeal> {
        val statuses = appealStatusCrudRepository.findAllByMilestoneGreaterThanEqual(2)
        return appealCrudRepository.findAllByStatusInAndPublished(statuses, true)
    }

    override fun create(appealDTO: AppealDTO) : Long? {
        val user = userCrudRepository.findByEmail("student@edu.hse.ru").get()
        val appealStatus = appealStatusCrudRepository.findByName("moderation").get()
        val appealType = if (appealDTO.type == "complaint") {
            appealTypeCrudRepository.findByName("complaint").get()
        } else {
            appealTypeCrudRepository.findByName("proposal").get()
        }

        val appeal = Appeal(
                null,
                appealDTO.title,
                appealDTO.text,
                user,
                appealDTO.feedback,
                appealDTO.published,
                appealType,
                appealStatus
        )
        appealCrudRepository.save(appeal)

        return appeal.id
    }

    override fun edit(appealDTO: AppealDTO) {
        if (appealDTO.id == null) {
            throw BadRequestException("Appeal ID not given")
        }
        val appealOptional = appealCrudRepository.findById(appealDTO.id)
        if (appealOptional.isEmpty) {
            throw NotFoundException("Appeal not found")
        }

        val appealType = if (appealDTO.type == "complaint") {
            appealTypeCrudRepository.findByName("complaint").get()
        } else {
            appealTypeCrudRepository.findByName("proposal").get()
        }

        val appeal = appealOptional.get()
        appeal.title = appealDTO.title
        appeal.text = appealDTO.text
        appeal.published = appealDTO.published
        appeal.type = appealType
        appeal.updated = Date()

        appealCrudRepository.save(appeal)
    }

    override fun changeStatus(id: Long, status: String) {
        val appealStatusOptional = appealStatusCrudRepository.findByName(status)
        if (appealStatusOptional.isEmpty) {
            throw BadRequestException("Status with given name not found")
        }
        val appealStatus = appealStatusOptional.get()

        val appealOptional = appealCrudRepository.findById(id)
        if (appealOptional.isEmpty) {
            throw NotFoundException("Appeal not found")
        }
        val appeal = appealOptional.get()

        if (appeal.status.milestone >= appealStatus.milestone) {
            throw BadRequestException("You cannot roll back to previous milestone")
        }

        appeal.status = appealStatus

        appealCrudRepository.save(appeal)
    }
}

