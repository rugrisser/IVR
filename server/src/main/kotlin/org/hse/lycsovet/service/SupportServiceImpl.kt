package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class SupportServiceImpl(
        private val userService: UserServiceImpl,
        private val ticketCrudRepository: TicketCrudRepository
) : SupportService {
    override fun get(token: String): List<Ticket> {
        return ticketCrudRepository.findAll()
    }

    override fun create(token: String, ticketDTO: TicketDTO) : Long? {
        if (userService.validate(token)) {
            if (!userService.checkRoleLevel(token, 1, 3)) throw ForbiddenException("You cannot create tickets")
            val user = userService.getUser(token)
            val ticket = Ticket(null, user, ticketDTO.text)

            ticketCrudRepository.save(ticket)
            return ticket.id
        }

        throw ForbiddenException("Token is invalid")
    }

    override fun close(token: String, id: Long, message: String) {
        if (userService.validate(token)) {
            if (!userService.checkRoleLevel(token, 4, 4)) throw ForbiddenException("You cannot close tickets")
            val ticketOptional = ticketCrudRepository.findById(id)
            if (ticketOptional.isEmpty) {
                throw NotFoundException("Ticket not found")
            }
            val ticket = ticketOptional.get()
            ticket.response = message
            ticket.opened = false
            ticket.updated = Date()
            ticketCrudRepository.save(ticket)
        } else {
            throw ForbiddenException("Token is invalid")
        }
    }
}