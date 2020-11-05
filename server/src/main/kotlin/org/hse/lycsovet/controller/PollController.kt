package org.hse.lycsovet.controller

import org.hse.lycsovet.AnswerDTO
import org.hse.lycsovet.Poll
import org.hse.lycsovet.PollDTO
import org.hse.lycsovet.service.PollServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/poll")
class PollController (
        private val pollService: PollServiceImpl
) {

    @GetMapping("/{id}")
    fun get(
            @PathVariable id: String
    ) : ResponseEntity<Poll> {
        return ResponseEntity.ok(pollService.get(id))
    }

    @GetMapping("/appeal/{id}")
    fun getListByAppeal(
            @PathVariable id: Long
    ) : ResponseEntity<Any> {
        return ResponseEntity.ok(pollService.getListByAppeal(id))
    }

    @GetMapping("/{id}/answers")
    fun answers(
            @RequestHeader("Authorization") token: String,
            @PathVariable id: String
    ) : ResponseEntity<Any> {
        return ResponseEntity.ok(pollService.answers(token, id))
    }

    @PostMapping
    fun create(
            @RequestHeader("Authorization") token: String,
            @RequestBody body: PollDTO
    ) : ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        result["id"] = pollService.create(token, body)

        return ResponseEntity.ok(result)
    }

    @PostMapping("/answer")
    fun answer(
            @RequestHeader("Authorization") token: String,
            @RequestBody body: AnswerDTO
    ) : ResponseEntity<Any> {
        pollService.answer(token, body)
        return ResponseEntity.noContent().build()
    }

    @PutMapping
    fun edit(
            @RequestHeader("Authorization") token: String,
            @RequestBody body: PollDTO
    ) : ResponseEntity<Any> {
        pollService.edit(token, body)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}/publish")
    fun publish(
            @RequestHeader("Authorization") token: String,
            @PathVariable id: String
    ) : ResponseEntity<Any> {
        pollService.publish(token, id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}/changeAvailability")
    fun changeAvailability(
            @RequestHeader("Authorization") token: String,
            @PathVariable id: String
    ) : ResponseEntity<Any> {
        pollService.changeAvailability(token, id)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun delete(
            @RequestHeader("Authorization") token: String,
            @PathVariable id: String
    ) : ResponseEntity<Any> {
        pollService.delete(token, id)
        return ResponseEntity.noContent().build()
    }
}