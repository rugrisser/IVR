package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.ArgumentCaptor

import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class SupportServiceImplTest {

    private val ticketCrudRepository = mock(TicketCrudRepository::class.java)
    private val userService = mock(UserServiceImpl::class.java)
    private val supportService = SupportServiceImpl(userService, ticketCrudRepository)

    @Test
    fun createSuccess() {
        val role = Role(
            1,
            "Test",
            1
        )
        val user = User(
            1,
            "",
            role,
            10,
            Stream.UNKNOWN,
            ""
        )

        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 1, 3)).thenReturn(true)
        `when`(userService.getUser("")).thenReturn(user)

        assertDoesNotThrow {
            supportService.create("", TicketDTO("Test"))
        }

        val ticketCaptor = ArgumentCaptor.forClass(Ticket::class.java)
        verify(ticketCrudRepository).save(ticketCaptor.capture())
        val capturedTickets = ticketCaptor.allValues
        val ticket = capturedTickets[0]
        assertEquals(true, ticket.opened)
        assertEquals("Test", ticket.text)
    }

    @Test
    fun createFailDueToLackOfAccess() {
        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 1, 3)).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            supportService.create("", TicketDTO(""))
        }
    }

    @Test
    fun createFailDueToWrongToken() {
        `when`(userService.validate("")).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            supportService.create("", TicketDTO(""))
        }
    }

    @Test
    fun closeSuccess() {
        val message = "Test"
        val role = Role(
            1,
            "Test",
            1
        )
        val user = User(
            1,
            "",
            role,
            10,
            Stream.UNKNOWN,
            ""
        )
        val ticket = Ticket(
            1,
            user,
            "",
            "",
            true
        )

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

        assertFailsWith(NotFoundException::class) {
            supportService.close("", 1, "Test")
        }
    }

    @Test
    fun closeFailDueToLackOfAccess() {
        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 4, 4)).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            supportService.close("", 1, "Test")
        }
    }

    @Test
    fun closeFailDueToWrongToken() {
        `when`(userService.validate("")).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            supportService.close("", 1, "Test")
        }
    }
}