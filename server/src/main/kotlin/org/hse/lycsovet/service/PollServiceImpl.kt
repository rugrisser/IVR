package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.slf4j.Logger
import org.springframework.stereotype.Service
import java.lang.ClassCastException

@Service
class PollServiceImpl (
        private val logger: Logger,
        private val userService: UserServiceImpl,
        private val pollCrudRepository: PollCrudRepository,
        private val answerCrudRepository: AnswerCrudRepository,
        private val appealCrudRepository: AppealCrudRepository
) : PollService {
    override fun get(id: String): Poll {
        val pollOptional = pollCrudRepository.findById(id)
        if (pollOptional.isEmpty) throw NotFoundException("Poll with given ID not found")

        return pollOptional.get()
    }

    override fun getListByAppeal(appealID: Long): List<Poll> {
        return pollCrudRepository.findAllByAppealID(appealID)
    }

    override fun answers(token: String, id: String): List<Answer> {
        if (userService.validate(token)) {
            if (!userService.checkRoleLevel(token, 2, 3)) throw ForbiddenException("You cannot watch poll answers")
            val pollOptional = pollCrudRepository.findById(id)
            if (pollOptional.isEmpty) throw NotFoundException("Poll with given ID not found")

            return answerCrudRepository.findAllByPollID(id)
        }

        throw ForbiddenException("Token is invalid")
    }

    override fun create(token: String, poll: PollDTO): String? {
        if (userService.validate(token)) {
            if (!userService.checkRoleLevel(token, 2, 3)) throw ForbiddenException("You cannot create polls")
            if (!validatePoll(poll)) throw BadRequestException("Poll is invalid")

            val pollDocument = Poll(poll)
            pollCrudRepository.save(pollDocument)

            return pollDocument.id
        }

        throw ForbiddenException("Token is invalid")
    }

    override fun publish(token: String, pollID: String) {
        if (userService.validate(token)) {
            if (!userService.checkRoleLevel(token, 2, 3)) throw ForbiddenException("You cannot publish polls")
            val pollOptional = pollCrudRepository.findById(pollID)
            if (pollOptional.isEmpty) {
                throw NotFoundException("Poll with given ID not found")
            }
            val poll = pollOptional.get()

            if (poll.published) {
                throw BadRequestException("Poll has already published")
            }
            poll.published = true
            pollCrudRepository.save(poll)
        } else {
            throw ForbiddenException("Token is invalid")
        }
    }

    override fun edit(token: String, poll: PollDTO) {
        if (userService.validate(token)) {
            if (!userService.checkRoleLevel(token, 2, 3)) throw ForbiddenException("You cannot edit polls")
            if (poll.id == null) throw BadRequestException("Given null poll ID")

            val pollOptional = pollCrudRepository.findById(poll.id)
            if (pollOptional.isEmpty) throw NotFoundException("Poll with given ID not found")
            val pollDocument = pollOptional.get()

            if (!validatePoll(poll)) throw BadRequestException("Poll is invalid")

            val answers = answerCrudRepository.findAllByPollID(poll.id)
            if (answers.isNotEmpty()) throw BadRequestException("Poll has already been answered")

            pollDocument.update(poll)
            pollCrudRepository.save(pollDocument)
        } else {
            throw ForbiddenException("Token is invalid")
        }
    }

    override fun delete(token: String, pollID: String) {
        if (userService.validate(token)) {
            if (!userService.checkRoleLevel(token, 2, 3)) throw ForbiddenException("You cannot delete polls")
            val pollOptional = pollCrudRepository.findById(pollID)
            if (pollOptional.isEmpty) throw NotFoundException("Poll with given ID not found")
            val poll = pollOptional.get()

            answerCrudRepository.deleteAllByPollID(poll.id!!)
            pollCrudRepository.delete(poll)
        } else {
            throw ForbiddenException("Token is invalid")
        }
    }

    override fun answer(token: String, answer: AnswerDTO) {
        if (userService.validate(token)) {
            if (!userService.checkRoleLevel(token, 1, 1)) throw ForbiddenException("You cannot answer polls")
            if (validateAnswer(answer)) {
                val answerDocument = Answer(answer, 1)
                answerCrudRepository.save(answerDocument)
            } else {
                throw BadRequestException("Answer is invalid")
            }
        } else {
            throw ForbiddenException("Token is invalid")
        }
    }

    override fun changeAvailability(token: String, pollID: String) {
        if (userService.validate(token)) {
            if (!userService.checkRoleLevel(token, 2, 3)) throw ForbiddenException("You cannot change the availability of poll")
            val pollOptional = pollCrudRepository.findById(pollID)
            if (pollOptional.isEmpty) throw NotFoundException("Poll with given ID not found")

            val poll = pollOptional.get()
            poll.available = !poll.available
            pollCrudRepository.save(poll)
        } else {
            throw ForbiddenException("Token is invalid")
        }
    }

    private fun validateAnswer(answer: AnswerDTO) : Boolean {
        val pollOptional = pollCrudRepository.findById(answer.pollID)
        if (pollOptional.isEmpty) throw NotFoundException("Poll with given ID not found")
        val poll = pollOptional.get()

        if (answer.content.size != poll.content.size) {
            logger.info("[MILESTONES SIZE] Not equal!")
            return false
        }

        for (i in answer.content.indices) {
            if (answer.content[i].size != poll.content[i].size) {
                logger.info("[QUESTION SIZE] Not equal!")
                return false
            }
            for (j in answer.content[i].indices) {
                try {
                    when (poll.content[i][j].type) {
                        QuestionType.RADIO -> {
                            val value: Int = answer.content[i][j] as Int
                            val questionContent: List<String> = poll.content[i][j].mixed["content"] as List<String>
                            if (value < 0 || value >= questionContent.size) return false
                        }
                        QuestionType.CHECKBOXES -> {
                            val values: List<Int> = answer.content[i][j] as List<Int>
                            val questionContent: List<String> = poll.content[i][j].mixed["content"] as List<String>
                            val checkboxesSize = questionContent.size

                            for (value in values) {
                                if (value < 0 || value >= checkboxesSize) return false
                            }
                        }
                        QuestionType.SCALE -> {
                            val value: Int = answer.content[i][j] as Int
                            val size: Int = poll.content[i][j].mixed["size"] as Int

                            if (value < 0 || value > size) return false
                        }
                        else -> {
                            if (answer.content[i][j] !is String) return false
                        }
                    }
                } catch (classCastException: ClassCastException) {
                    return false
                }
            }
        }

        return true
    }

    private fun validatePoll(poll: PollDTO) : Boolean {
        if (poll.appealID != null) {
            val appealOptional = appealCrudRepository.findById(poll.appealID)
            if (appealOptional.isEmpty) return false
        }

        for (questionList in poll.content) {
            for (question in questionList) {
                try {
                    when (question.type) {
                        QuestionType.RADIO -> {
                            val content: List<String> = question.mixed["content"] as List<String>
                            if (content.isEmpty()) return false
                        }
                        QuestionType.CHECKBOXES -> {
                            val content: List<String> = question.mixed["content"] as List<String>
                            if (content.isEmpty()) return false
                        }
                        QuestionType.SCALE -> {
                            if (question.mixed["size"] !is Int) return false
                        }
                    }
                } catch (classCastException: ClassCastException) {
                    return false
                }
            }
        }

        return true
    }
}