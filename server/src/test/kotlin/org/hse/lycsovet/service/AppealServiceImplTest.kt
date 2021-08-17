package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import java.util.*

internal class AppealServiceImplTest {

    private val userService = mock(UserServiceImpl::class.java)
    private val appealCrudRepository = mock(AppealCrudRepository::class.java)
    private val appealTypeCrudRepository = mock(AppealTypeCrudRepository::class.java)
    private val appealStatusCrudRepository = mock(AppealStatusCrudRepository::class.java)
    private val appealService = AppealServiceImpl(
        userService,
        appealCrudRepository,
        appealTypeCrudRepository,
        appealStatusCrudRepository
    )

    @Test
    fun createSuccess() {
        val dto = createAppealDTO(
            id = null,
            title = "Title",
            text = "Text",
            feedback = 1,
            published = false
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.getUser(anyString())).thenReturn(createUser())
        `when`(userService.checkRoleLevel(anyString(), eq(1), eq(1))).thenReturn(true)
        `when`(appealStatusCrudRepository.findByName(anyString())).thenReturn(Optional.of(createAppealStatus()))
        `when`(appealTypeCrudRepository.findByName(anyString())).thenReturn(Optional.of(createAppealType()))

        assertDoesNotThrow {
            appealService.create("", dto)
        }

        val appealCaptor = ArgumentCaptor.forClass(Appeal::class.java)
        verify(appealCrudRepository, times(1)).save(appealCaptor.capture())
        val capturedAppeals = appealCaptor.allValues
        val appeal = capturedAppeals[0]

        assertEquals(dto.title, appeal.title)
        assertEquals(dto.text, appeal.text)
        assertEquals(dto.feedback, appeal.feedback)
        assertEquals(dto.published, appeal.published)
    }

    @Test
    fun createFailDueToLackOfAccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(1), eq(1))).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            appealService.create("", createAppealDTO())
        }
        verify(appealCrudRepository, times(0)).save(any(Appeal::class.java))
    }

    @Test
    fun createFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            appealService.create("", createAppealDTO())
        }
        verify(appealCrudRepository, times(0)).save(any(Appeal::class.java))
    }

    @Test
    fun editSuccess() {
        val appeal = createAppeal(
            id = 1,
            title = "Old title",
            text = "Old text",
            user = createUser(id = 1),
            feedback = 0,
            published = false
        )
        val dto = createAppealDTO(
            id = 1,
            title = "New title",
            text = "New text",
            feedback = 2,
            published = true
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(appealCrudRepository.findById(anyLong())).thenReturn(Optional.of(appeal))
        `when`(appealTypeCrudRepository.findByName(anyString())).thenReturn(Optional.of(createAppealType()))
        `when`(userService.getUser(anyString())).thenReturn(createUser(id = 1))

        assertDoesNotThrow {
            appealService.edit("", dto)
        }
        assertEquals("New title", appeal.title)
        assertEquals("New text", appeal.text)
        assertEquals(true, appeal.published)
        verify(appealCrudRepository, times(1)).save(any(Appeal::class.java))
    }

    @Test
    fun editFailDueToWrongDTO() {
        `when`(userService.validate(anyString())).thenReturn(true)

        assertThrows(BadRequestException::class.java) {
            appealService.edit("", createAppealDTO())
        }
        verify(appealCrudRepository, times(0)).save(any(Appeal::class.java))
    }

    @Test
    fun editFailDueToMissedAppeal() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(appealCrudRepository.findById(anyLong())).thenReturn(Optional.empty())

        assertThrows(NotFoundException::class.java) {
            appealService.edit("", createAppealDTO(id = 1))
        }
        verify(appealCrudRepository, times(0)).save(any(Appeal::class.java))
    }

    @Test
    fun editFailDueToDifferentUsers() {
        val appeal = createAppeal(
            user = createUser(id = 1)
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(appealCrudRepository.findById(anyLong())).thenReturn(Optional.of(appeal))
        `when`(appealTypeCrudRepository.findByName(anyString())).thenReturn(Optional.of(createAppealType()))
        `when`(userService.getUser(anyString())).thenReturn(createUser(id = 2))

        assertThrows(ForbiddenException::class.java) {
            appealService.edit("", createAppealDTO(id = 1))
        }
        verify(appealCrudRepository, times(0)).save(any(Appeal::class.java))
    }

    @Test
    fun editFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            appealService.edit("", createAppealDTO())
        }
        verify(appealCrudRepository, times(0)).save(any(Appeal::class.java))
    }

    @Test
    fun changeStatusSuccess() {
        val appeal = createAppeal(
            status = createAppealStatus(
                id = 1,
                name = "Current status",
                milestone = 1
            )
        )
        val appealStatus = createAppealStatus(
            id = 2,
            name = "Desired status",
            milestone = 2
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(appealStatusCrudRepository.findByName(anyString())).thenReturn(Optional.of(appealStatus))
        `when`(appealCrudRepository.findById(anyLong())).thenReturn(Optional.of(appeal))

        assertDoesNotThrow {
            appealService.changeStatus("", 1, "")
        }
        assertEquals(2, appeal.status.milestone)
        verify(appealCrudRepository, times(1)).save(any(Appeal::class.java))
    }

    @Test
    fun changeStatusFailDueToMilestoneRollback() {
        val appeal = createAppeal(
            status = createAppealStatus(
                id = 2,
                name = "Current status",
                milestone = 2
            )
        )
        val appealStatus = createAppealStatus(
            id = 1,
            name = "Desired status",
            milestone = 1
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(appealStatusCrudRepository.findByName(anyString())).thenReturn(Optional.of(appealStatus))
        `when`(appealCrudRepository.findById(anyLong())).thenReturn(Optional.of(appeal))

        assertThrows(BadRequestException::class.java) {
            appealService.changeStatus("", 1, "")
        }
        verify(appealCrudRepository, times(0)).save(any(Appeal::class.java))
    }

    @Test
    fun changeStatusFailDueToMissedAppeal() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(appealStatusCrudRepository.findByName(anyString())).thenReturn(Optional.of(createAppealStatus()))
        `when`(appealCrudRepository.findById(anyLong())).thenReturn(Optional.empty())

        assertThrows(NotFoundException::class.java) {
            appealService.changeStatus("", 1, "")
        }
        verify(appealCrudRepository, times(0)).save(any(Appeal::class.java))
    }

    @Test
    fun changeStatusFailDueToMissedAppealStatus() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(true)
        `when`(appealStatusCrudRepository.findByName(anyString())).thenReturn(Optional.empty())

        assertThrows(BadRequestException::class.java) {
            appealService.changeStatus("", 1, "")
        }
        verify(appealCrudRepository, times(0)).save(any(Appeal::class.java))
    }

    @Test
    fun changeStatusFailDueToLackOfAccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(3))).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            appealService.changeStatus("", 1, "")
        }
        verify(appealCrudRepository, times(0)).save(any(Appeal::class.java))
    }

    @Test
    fun changeStatusFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            appealService.changeStatus("", 1, "")
        }
        verify(appealCrudRepository, times(0)).save(any(Appeal::class.java))
    }

    private fun createUser(
        id: Long? = 1,
        login: String = "",
        role: Role = createRole(),
        grade: Int = 10,
        stream: Stream = Stream.UNKNOWN,
        name: String = ""
    ) = User(id, login, role, grade, stream, name)

    private fun createRole(
        id: Long? = 1,
        name: String = "",
        level: Int = 1
    ) = Role(id, name, level)

    private fun createAppeal(
        id: Long? = 1,
        title: String = "",
        text: String = "",
        user: User = createUser(),
        feedback: Int = 0,
        published: Boolean = true,
        type: AppealType = createAppealType(),
        status: AppealStatus = createAppealStatus()
    ) = Appeal(id, title, text, user, feedback, published, type, status)

    private fun createAppealDTO(
        id: Long? = null,
        title: String = "",
        text: String = "",
        feedback: Int = 0,
        published: Boolean = true,
        type: String = ""
    ) = AppealDTO(id, title, text, feedback, published, type)

    private fun createAppealType(
        id: Long? = 1,
        name: String = ""
    ) = AppealType(id, name)

    private fun createAppealStatus(
        id: Long? = 1,
        name: String = "",
        milestone: Long = 1
    ) = AppealStatus(id, name, milestone)
}