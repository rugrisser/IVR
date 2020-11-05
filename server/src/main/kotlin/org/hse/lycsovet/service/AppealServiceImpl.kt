package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppealServiceImpl (
        private val userService: UserServiceImpl,
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

    override fun own(token: String): List<Appeal> {
        if (userService.validate(token)) {
            val user = userService.getUser(token)
            return appealCrudRepository.findAllByAuthor(user)
        }

        throw ForbiddenException("Token is invalid")
    }

    override fun feed(): List<Appeal> {
        val statuses = appealStatusCrudRepository.findAllByMilestoneGreaterThanEqual(2)
        return appealCrudRepository.findAllByStatusInAndPublished(statuses, true)
    }

    override fun create(token: String, appealDTO: AppealDTO) : Long? {
        if (userService.validate(token)) {
            val user = userService.getUser(token)
            if (!userService.checkRoleLevel(token, 1, 1)) throw ForbiddenException("You cannot create appeals")
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

        throw ForbiddenException("Token is invalid")
    }

    override fun edit(token: String, appealDTO: AppealDTO) {
        if (userService.validate(token)) {
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

            val user = userService.getUser(token)
            val appeal = appealOptional.get()
            if (appeal.author.id != user.id) throw ForbiddenException("You cannot edit this appeal")

            appeal.title = appealDTO.title
            appeal.text = appealDTO.text
            appeal.published = appealDTO.published
            appeal.type = appealType
            appeal.updated = Date()

            appealCrudRepository.save(appeal)
        } else {
            throw ForbiddenException("Token is invalid")
        }
    }

    override fun changeStatus(token: String, id: Long, status: String) {
        if (userService.validate(token)) {
            if (!userService.checkRoleLevel(token, 2, 3)) throw ForbiddenException("You cannot change status")
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
        } else {
            throw ForbiddenException("Token is invalid")
        }
    }
}

