package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.*
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

internal class PollServiceImplTest {

    private val logger = LoggerFactory.getLogger(PollServiceImplTest::class.java)
    private val userService = mock(UserServiceImpl::class.java)
    private val pollCrudRepository = mock(PollCrudRepository::class.java)
    private val answerCrudRepository = mock(AnswerCrudRepository::class.java)
    private val appealCrudRepository = mock(AppealCrudRepository::class.java)
    private val pollService = PollServiceImpl(
        logger,
        userService,
        pollCrudRepository,
        answerCrudRepository,
        appealCrudRepository
    )


    @Test
    fun createSuccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(appealCrudRepository.findById(anyLong())).thenReturn(Optional.of(createAppeal()))

        val questions = ArrayList<Question>()

        val radioOptionList = arrayListOf("First", "Second")
        val radioMixed = HashMap<String, Any?>()
        radioMixed["content"] = radioOptionList
        questions.add(createQuestion(
            type = QuestionType.RADIO,
            mixed = radioMixed
        ))

        val checkboxesOptionList = arrayListOf("First", "Second")
        val checkboxesMixed = HashMap<String, Any?>()
        checkboxesMixed["content"] = checkboxesOptionList
        questions.add(createQuestion(
            type = QuestionType.CHECKBOXES,
            mixed = checkboxesMixed
        ))

        val scaleMixed = HashMap<String, Any?>()
        scaleMixed["size"] = 10
        questions.add(createQuestion(
            type = QuestionType.SCALE,
            mixed = scaleMixed
        ))

        val content = ArrayList<List<Question>>()
        content.add(questions)
        val dto = createPollDTO(
            content = content,
            appealID = 1,
            available = false,
            published = false
        )

        assertDoesNotThrow {
            pollService.create("", dto)
        }

        val pollCaptor = ArgumentCaptor.forClass(Poll::class.java)
        verify(pollCrudRepository, times(1)).save(pollCaptor.capture())
        val capturedPolls = pollCaptor.allValues
        val poll = capturedPolls[0]

        assertEquals(dto.available, poll.available)
        assertEquals(dto.published, poll.published)
        assertEquals(dto.appealID, poll.appealID)
        assertEquals(1, poll.content.size)
        assertEquals(3, poll.content[0].size)
    }

    @Test
    fun createFailDueToInvalidScale() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)

        val questions = ArrayList<Question>().apply {
            val mixed = HashMap<String, Any?>()
            mixed["size"] = ""
            add(createQuestion(
                type = QuestionType.SCALE,
                mixed = mixed
            ))
        }
        val content = ArrayList<List<Question>>().apply { add(questions) }
        val dto = createPollDTO(
            content = content
        )
        assertThrows(BadRequestException::class.java) {
            pollService.create("", dto)
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun createFailDueToInvalidCheckboxes() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)

        // CASE 1: Wrong mixed content

        val mixed = HashMap<String, Any?>()
        mixed["content"] = ""
        val questions = ArrayList<Question>().apply {
            add(createQuestion(
                    type = QuestionType.CHECKBOXES,
                    mixed = mixed
            ))
        }
        val content = ArrayList<List<Question>>().apply { add(questions) }
        val dto = createPollDTO(
            content = content
        )
        assertThrows(BadRequestException::class.java) {
            pollService.create("", dto)
        }

        // CASE 2: Empty list in mixed.content

        mixed["content"] = ArrayList<String>()
        assertThrows(BadRequestException::class.java) {
            pollService.create("", dto)
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun createFailDueToInvalidRadio() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)

        // CASE 1: Wrong mixed content

        val mixed = HashMap<String, Any?>()
        mixed["content"] = ""
        val questions = ArrayList<Question>().apply {
            add(createQuestion(
                type = QuestionType.RADIO,
                mixed = mixed
            ))
        }
        val content = ArrayList<List<Question>>().apply {
            add(questions)
        }
        val dto = createPollDTO(
            content = content
        )
        assertThrows(BadRequestException::class.java) {
            pollService.create("", dto)
        }

        // CASE 2: Empty list in mixed.content

        mixed["content"] = ArrayList<String>()
        assertThrows(BadRequestException::class.java) {
            pollService.create("", dto)
        }

        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun createFailDueToMissedAppeal() {
        val dto = createPollDTO(
            appealID = 1
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(appealCrudRepository.findById(anyLong())).thenReturn(Optional.empty())

        assertThrows(BadRequestException::class.java) {
            pollService.create("", dto)
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun createFailDueToLackOfAccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            pollService.create("", createPollDTO())
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun createFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            pollService.create("", createPollDTO())
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun publishSuccess() {
        val dto = createPoll(published = false)

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(dto))

        assertDoesNotThrow {
            pollService.publish("", "")
        }
        assertEquals(true, dto.published)
        verify(pollCrudRepository, times(1)).save(any(Poll::class.java))
    }

    @Test
    fun publishFailDueToPublishedPoll() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(createPoll(published = true)))

        assertThrows(BadRequestException::class.java) {
            pollService.publish("", "")
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun publishFailDueToMissedPoll() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.empty())

        assertThrows(NotFoundException::class.java) {
            pollService.publish("", "")
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun publishFailDueToLackOfAccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            pollService.publish("", "")
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun publishFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            pollService.publish("", "")
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun editSuccess() {
        val poll = createPoll()

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(poll))
        `when`(appealCrudRepository.findById(anyLong())).thenReturn(Optional.of(createAppeal()))
        `when`(answerCrudRepository.findAllByPollID(anyString())).thenReturn(ArrayList())

        val questions = ArrayList<Question>()

        val radioOptionList = arrayListOf("First", "Second")
        val radioMixed = HashMap<String, Any?>()
        radioMixed["content"] = radioOptionList
        questions.add(createQuestion(
            type = QuestionType.RADIO,
            mixed = radioMixed
        ))

        val checkboxesOptionList = arrayListOf("First", "Second")
        val checkboxesMixed = HashMap<String, Any?>()
        checkboxesMixed["content"] = checkboxesOptionList
        questions.add(createQuestion(
            type = QuestionType.CHECKBOXES,
            mixed = checkboxesMixed
        ))

        val scaleMixed = HashMap<String, Any?>()
        scaleMixed["size"] = 10
        questions.add(createQuestion(
            type = QuestionType.SCALE,
            mixed = scaleMixed
        ))

        val content = ArrayList<List<Question>>()
        content.add(questions)
        val dto = createPollDTO(
            id = "",
            content = content,
            appealID = 1
        )

        assertDoesNotThrow {
            pollService.edit("", dto)
        }
        assertEquals(1, poll.content.size)
        assertEquals(3, poll.content[0].size)
        verify(pollCrudRepository, times(1)).save(any(Poll::class.java))
    }

    @Test
    fun editFailDueToAnswersExistence() {
        val answers = ArrayList<Answer>().apply { add(createAnswer()) }

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(createPoll()))
        `when`(answerCrudRepository.findAllByPollID(anyString())).thenReturn(answers)

        assertThrows(BadRequestException::class.java) {
            pollService.edit("", createPollDTO(id = ""))
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun editFailDueToInvalidScale() {
        val questions = ArrayList<Question>().apply {
            val mixed = HashMap<String, Any?>()
            mixed["size"] = ""
            add(createQuestion(
                type = QuestionType.SCALE,
                mixed = mixed
            ))
        }
        val content = ArrayList<List<Question>>().apply { add(questions) }
        val dto = createPollDTO(
            id = "",
            content = content
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(createPoll()))
        `when`(answerCrudRepository.findAllByPollID(anyString())).thenReturn(ArrayList())

        assertThrows(BadRequestException::class.java) {
            pollService.edit("", dto)
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun editFailDueToInvalidCheckboxes() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(createPoll()))
        `when`(answerCrudRepository.findAllByPollID(anyString())).thenReturn(ArrayList())

        // CASE 1: Wrong mixed content

        val mixed = HashMap<String, Any?>()
        mixed["content"] = ""
        val questions = ArrayList<Question>().apply {
            add(createQuestion(
                type = QuestionType.CHECKBOXES,
                mixed = mixed
            ))
        }
        val content = ArrayList<List<Question>>().apply { add(questions) }
        val dto = createPollDTO(
            id = "",
            content = content
        )
        assertThrows(BadRequestException::class.java) {
            pollService.edit("", dto)
        }

        // CASE 2: Empty list in mixed.content

        mixed["content"] = ArrayList<String>()
        assertThrows(BadRequestException::class.java) {
            pollService.edit("", dto)
        }

        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun editFailDueToInvalidRadio() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(createPoll()))
        `when`(answerCrudRepository.findAllByPollID(anyString())).thenReturn(ArrayList())

        // CASE 1: Wrong mixed content

        val mixed = HashMap<String, Any?>()
        mixed["content"] = ""
        val questions = ArrayList<Question>().apply {
            add(createQuestion(
                type = QuestionType.RADIO,
                mixed = mixed
            ))
        }
        val content = ArrayList<List<Question>>().apply { add(questions) }
        val dto = createPollDTO(
            id = "",
            content = content
        )
        assertThrows(BadRequestException::class.java) {
            pollService.edit("", dto)
        }

        // CASE 2: Empty list in mixed.content

        mixed["content"] = ArrayList<String>()
        assertThrows(BadRequestException::class.java) {
            pollService.edit("", dto)
        }

        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun editFailDueToMissedAppeal() {
        val dto = createPollDTO(
            id = "",
            appealID = 1
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(createPoll()))
        `when`(answerCrudRepository.findAllByPollID(anyString())).thenReturn(ArrayList())
        `when`(appealCrudRepository.findById(anyLong())).thenReturn(Optional.empty())

        assertThrows(BadRequestException::class.java) {
            pollService.edit("", dto)
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun editFailDueToMissedPoll() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.empty())

        assertThrows(NotFoundException::class.java) {
            pollService.edit("", createPollDTO(id = ""))
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun editFailDueToWrongDTO() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.empty())

        assertThrows(BadRequestException::class.java) {
            pollService.edit("", createPollDTO(id = null))
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun editFailDueToLackOfAccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            pollService.edit("", createPollDTO())
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun editFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            pollService.edit("", createPollDTO())
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun deleteSuccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(createPoll(id = "")))

        assertDoesNotThrow {
            pollService.delete("", "")
        }
        verify(answerCrudRepository, times(1)).deleteAllByPollID(anyString())
        verify(pollCrudRepository, times(1)).delete(any(Poll::class.java))
    }

    @Test
    fun deleteFailDueToMissedPoll() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.empty())

        assertThrows(NotFoundException::class.java) {
            pollService.delete("", "")
        }
        verify(answerCrudRepository, times(0)).deleteAllByPollID(anyString())
        verify(pollCrudRepository, times(0)).delete(any(Poll::class.java))
    }

    @Test
    fun deleteFailDueToLackOfAccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            pollService.delete("", "")
        }
        verify(answerCrudRepository, times(0)).deleteAllByPollID(anyString())
        verify(pollCrudRepository, times(0)).delete(any(Poll::class.java))
    }

    @Test
    fun deleteFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            pollService.delete("", "")
        }
        verify(answerCrudRepository, times(0)).deleteAllByPollID(anyString())
        verify(pollCrudRepository, times(0)).delete(any(Poll::class.java))
    }

    @Test
    fun answerSuccess() {
        val firstPollMilestone = ArrayList<Question>().apply {
            val scaleMixed = HashMap<String, Any?>()
            scaleMixed["size"] = 10
            add(createQuestion(
                type = QuestionType.SCALE,
                mixed = scaleMixed
            ))

            val checkboxesMixed = HashMap<String, Any?>()
            checkboxesMixed["content"] = arrayListOf("First", "Second", "Third")
            add(createQuestion(
                type = QuestionType.CHECKBOXES,
                mixed = checkboxesMixed
            ))
        }
        val secondPollMilestone = ArrayList<Question>().apply {
            val radioMixed = HashMap<String, Any?>()
            radioMixed["content"] = arrayListOf("First", "Second", "Third")
            add(createQuestion(
                type = QuestionType.RADIO,
                mixed = radioMixed
            ))
        }
        val poll = createPoll(
            id = "",
            content = arrayListOf(firstPollMilestone, secondPollMilestone)
        )
        val dto = createAnswerDTO(
            pollID = "",
            content = arrayListOf(
                arrayListOf<Any?>(5, arrayListOf(0, 2)),
                arrayListOf(1)
            )
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(1), eq(1))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(poll))

        assertDoesNotThrow {
            pollService.answer("", dto)
        }

        val answerCaptor = ArgumentCaptor.forClass(Answer::class.java)
        verify(answerCrudRepository, times(1)).save(answerCaptor.capture())
        val capturedAnswers = answerCaptor.allValues
        val answer = capturedAnswers[0]

        assertEquals(2, answer.content.size)
        assertEquals(2, answer.content[0].size)
        assertEquals(1, answer.content[1].size)
    }

    @Test
    fun answerFailDueToInvalidScaleAnswer() {
        val pollMilestone = ArrayList<Question>().apply {
            val mixed = HashMap<String, Any?>()
            mixed["size"] = 10
            add(createQuestion(
                type = QuestionType.SCALE,
                mixed = mixed
            ))
        }
        val answerMilestone = arrayListOf<Any?>(99)
        val poll = createPoll(
            id = "",
            content = arrayListOf(pollMilestone)
        )
        val dto = createAnswerDTO(
            pollID = "",
            content = arrayListOf(answerMilestone)
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(1), eq(1))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(poll))

        assertThrows(BadRequestException::class.java) {
            pollService.answer("", dto)
        }
        verify(answerCrudRepository, times(0)).save(any(Answer::class.java))
    }

    @Test
    fun answerFailDueToInvalidCheckboxesAnswer() {
        val pollMilestone = ArrayList<Question>().apply {
            val mixed = HashMap<String, Any?>()
            mixed["content"] = arrayListOf("First", "Second", "Third")
            add(createQuestion(
                type = QuestionType.CHECKBOXES,
                mixed = mixed
            ))
        }
        val answerMilestone = arrayListOf<Any?>(arrayListOf(0, 2, 1337))
        val poll = createPoll(
            id = "",
            content = arrayListOf(pollMilestone)
        )
        val dto = createAnswerDTO(
            pollID = "",
            content = arrayListOf(answerMilestone)
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(1), eq(1))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(poll))

        assertThrows(BadRequestException::class.java) {
            pollService.answer("", dto)
        }
        verify(answerCrudRepository, times(0)).save(any(Answer::class.java))
    }

    @Test
    fun answerFailDueToInvalidRadioAnswer() {
        val pollMilestone = ArrayList<Question>().apply {
            val mixed = HashMap<String, Any?>()
            mixed["content"] = arrayListOf("First", "Second", "Third")
            add(createQuestion(
                type = QuestionType.RADIO,
                mixed = mixed
            ))
        }
        val answerMilestone = arrayListOf<Any?>(10)
        val poll = createPoll(
            id = "",
            content = arrayListOf(pollMilestone)
        )
        val dto = createAnswerDTO(
            pollID = "",
            content = arrayListOf(answerMilestone)
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(1), eq(1))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(poll))

        assertThrows(BadRequestException::class.java) {
            pollService.answer("", dto)
        }
        verify(answerCrudRepository, times(0)).save(any(Answer::class.java))
    }

    @Test
    fun answerFailDueToDifferentMilestonesCount() {
        val pollContent = ArrayList<List<Question>>().apply { for (i in 0..2) add(ArrayList()) }
        val answerContent = ArrayList<List<Any?>>().apply { for (i in 0..3) add(ArrayList()) }
        val poll = createPoll(
            id = "",
            content = pollContent
        )
        val dto = createAnswerDTO(
            pollID = "",
            content = answerContent
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(1), eq(1))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(poll))

        assertThrows(BadRequestException::class.java) {
            pollService.answer("", dto)
        }
        verify(answerCrudRepository, times(0)).save(any(Answer::class.java))
    }

    @Test
    fun answerFailDueToDifferentQuestionsCount() {
        val pollMilestone = ArrayList<Question>().apply { for (i in 0..2) add(createQuestion()) }
        val answerMilestone = ArrayList<Question>().apply { for (i in 0..3) add(createQuestion()) }
        val pollContent = ArrayList<List<Question>>().apply { add(pollMilestone) }
        val answerContent = ArrayList<List<Any?>>().apply { add(answerMilestone) }

        val poll = createPoll(
            id = "",
            content = pollContent
        )
        val dto = createAnswerDTO(
            pollID = "",
            content = answerContent
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(1), eq(1))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(poll))

        assertThrows(BadRequestException::class.java) {
            pollService.answer("", dto)
        }
        verify(answerCrudRepository, times(0)).save(any(Answer::class.java))
    }

    @Test
    fun answerFailDueToLackOfAccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(1), eq(1))).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            pollService.answer("", createAnswerDTO())
        }
        verify(answerCrudRepository, times(0)).save(any(Answer::class.java))
    }

    @Test
    fun answerFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            pollService.answer("", createAnswerDTO())
        }
        verify(answerCrudRepository, times(0)).save(any(Answer::class.java))
    }

    @Test
    fun changeAvailabilitySuccess() {
        val poll = createPoll(
            available = false
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.of(poll))

        assertDoesNotThrow {
            pollService.changeAvailability("", "")
        }
        assertEquals(true, poll.available)
        verify(pollCrudRepository, times(1)).save(any(Poll::class.java))
    }

    @Test
    fun changeAvailabilityFailDueToMissedPoll() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(pollCrudRepository.findById(anyString())).thenReturn(Optional.empty())

        assertThrows(NotFoundException::class.java) {
            pollService.changeAvailability("", "")
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun changeAvailabilityFailDueToLackOfAccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            pollService.changeAvailability("", "")
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    @Test
    fun changeAvailabilityFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            pollService.changeAvailability("", "")
        }
        verify(pollCrudRepository, times(0)).save(any(Poll::class.java))
    }

    private fun createQuestion(
        name: String = "",
        type: QuestionType = QuestionType.TEXT,
        compulsory: Boolean = false,
        mixed: Map<String, Any?> = HashMap()
    ) = Question(name, type, compulsory, mixed)

    private fun createPoll(
        id: String? = null,
        appealID: Long? = null,
        available: Boolean = false,
        published: Boolean = false,
        target: List<String> = ArrayList(),
        content: List<List<Question>> = ArrayList()
    ) = Poll(id, appealID, available, published, target, content)

    private fun createAnswer(
        id: String? = null,
        userID: Long = 1,
        pollID: String = "",
        content: List<List<Any?>> = ArrayList()
    ) = Answer(id, userID, pollID, content)

    private fun createPollDTO(
        id: String? = null,
        appealID: Long? = null,
        available: Boolean = false,
        published: Boolean = false,
        target: List<String> = ArrayList(),
        content: List<List<Question>> = ArrayList()
    ) = PollDTO(id, appealID, available, published, target, content)

    private fun createAnswerDTO(
        pollID: String = "",
        content: List<List<Any?>> = ArrayList()
    ) = AnswerDTO(pollID, content)
}