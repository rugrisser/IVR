package org.hse.lycsovet.controller

import org.hse.lycsovet.Ticket
import org.hse.lycsovet.TicketDTO
import org.hse.lycsovet.service.SupportServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/support")
class SupportController(
        private val supportService: SupportServiceImpl
) {

    @GetMapping
    fun get() : ResponseEntity<List<Ticket>> {
        return ResponseEntity.ok(supportService.get())
    }

    @PostMapping
    fun create(@RequestBody body: TicketDTO) : ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        result["id"] = supportService.create(body)

        return ResponseEntity.ok(result)
    }

    @PutMapping("/{id}")
    fun close(@PathVariable id: Long, @RequestBody body: TicketDTO) : ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        supportService.close(id, body.text)
        result["status"] = "ok"

        return ResponseEntity.ok(result)
    }
}