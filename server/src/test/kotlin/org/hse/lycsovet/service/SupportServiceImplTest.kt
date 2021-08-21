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
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(1), eq(3))).thenReturn(true)
        `when`(userService.getUser(anyString())).thenReturn(createUser())

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
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(1), eq(3))).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            supportService.create("", TicketDTO(""))
        }
    }

    @Test
    fun createFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            supportService.create("", TicketDTO(""))
        }
    }

    @Test
    fun closeSuccess() {
        val message = "Test"
        val ticket = createTicket()

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(4), eq(4))).thenReturn(true)
        `when`(ticketCrudRepository.findById(anyLong())).thenReturn(Optional.of(ticket))

        assertDoesNotThrow {
            supportService.close("", 1, message)
        }
        assertEquals(message, ticket.response)
        assertEquals(false, ticket.opened)
    }

    @Test
    fun closeFailDueToMissedTicket() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(4), eq(4))).thenReturn(true)
        `when`(ticketCrudRepository.findById(anyLong())).thenReturn(Optional.empty())

        assertThrows(NotFoundException::class.java) {
            supportService.close("", 1, "Test")
        }
    }

    @Test
    fun closeFailDueToLackOfAccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(4), eq(4))).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            supportService.close("", 1, "Test")
        }
    }

    @Test
    fun closeFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            supportService.close("", 1, "Test")
        }
    }

    private fun createTicket(
        id: Long? = 1,
        user: User = createUser(),
        text: String = "",
        response: String = "",
        opened: Boolean = true
    ) = Ticket(id, user, text, response, opened)
}