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
    fun get(
            @RequestHeader("Authorization") token: String
    ) : ResponseEntity<List<Ticket>> {
        return ResponseEntity.ok(supportService.get(token))
    }

    @PostMapping
    fun create(
            @RequestHeader("Authorization") token: String,
            @RequestBody body: TicketDTO
    ) : ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        result["id"] = supportService.create(token, body)

        return ResponseEntity.ok(result)
    }

    @PutMapping("/{id}")
    fun close(
            @RequestHeader("Authorization") token: String,
            @PathVariable id: Long,
            @RequestBody body: TicketDTO
    ) : ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        supportService.close(token, id, body.text)
        result["status"] = "ok"

        return ResponseEntity.ok(result)
    }
}