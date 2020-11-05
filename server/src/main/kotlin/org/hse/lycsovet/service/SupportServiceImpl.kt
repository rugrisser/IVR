package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class SupportServiceImpl(
        private val userCrudRepository: UserCrudRepository,
        private val ticketCrudRepository: TicketCrudRepository
) : SupportService {
    override fun get(): List<Ticket> {
        return ticketCrudRepository.findAll()
    }

    override fun create(ticketDTO: TicketDTO) : Long? {
        val user = userCrudRepository.findByLogin("student@edu.hse.ru").get()
        val ticket = Ticket(null, user, ticketDTO.text)

        ticketCrudRepository.save(ticket)
        return ticket.id
    }

    override fun close(id: Long, message: String) {
        val ticketOptional = ticketCrudRepository.findById(id)
        if (ticketOptional.isEmpty) {
            throw NotFoundException("Ticket not found")
        }
        val ticket = ticketOptional.get()
        ticket.response = message
        ticket.opened = false
        ticket.updated = Date()
        ticketCrudRepository.save(ticket)
    }
}