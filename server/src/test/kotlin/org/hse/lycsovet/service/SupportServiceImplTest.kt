package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import java.util.*

internal class SupportServiceImplTest {

    private val ticketCrudRepository = mock(TicketCrudRepository::class.java)
    private val userService = mock(UserServiceImpl::class.java)
    private val supportService = SupportServiceImpl(userService, ticketCrudRepository)

    @Test
    fun createSuccess() {
        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 1, 3)).thenReturn(true)
        `when`(userService.getUser("")).thenReturn(createUser())

        assertDoesNotThrow {
            supportService.create("", TicketDTO("Test"))
        }

        val ticketCaptor = ArgumentCaptor.forClass(Ticket::class.java)
        verify(ticketCrudRepository, times(1)).save(ticketCaptor.capture())
        val capturedTickets = ticketCaptor.allValues
        val ticket = capturedTickets[0]
        assertEquals(true, ticket.opened)
        assertEquals("Test", ticket.text)
    }

    @Test
    fun createFailDueToLackOfAccess() {
        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 1, 3)).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            supportService.create("", TicketDTO(""))
        }
    }

    @Test
    fun createFailDueToWrongToken() {
        `when`(userService.validate("")).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            supportService.create("", TicketDTO(""))
        }
    }

    @Test
    fun closeSuccess() {
        val message = "Test"
        val ticket = createTicket()

        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 4, 4)).thenReturn(true)
        `when`(ticketCrudRepository.findById(1)).thenReturn(Optional.of(ticket))

        assertDoesNotThrow {
            supportService.close("", 1, message)
        }
        assertEquals(message, ticket.response)
        assertEquals(false, ticket.opened)
    }

    @Test
    fun closeFailDueToMissedTicket() {
        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 4, 4)).thenReturn(true)
        `when`(ticketCrudRepository.findById(1)).thenReturn(Optional.empty())

        assertThrows(NotFoundException::class.java) {
            supportService.close("", 1, "Test")
        }
    }

    @Test
    fun closeFailDueToLackOfAccess() {
        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 4, 4)).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            supportService.close("", 1, "Test")
        }
    }

    @Test
    fun closeFailDueToWrongToken() {
        `when`(userService.validate("")).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            supportService.close("", 1, "Test")
        }
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

    private fun createTicket(
        id: Long? = 1,
        user: User = createUser(),
        text: String = "",
        response: String = "",
        opened: Boolean = true
    ) = Ticket(id, user, text, response, opened)
}