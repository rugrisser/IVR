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
            @PathVariable id: String
    ) : ResponseEntity<Any> {
        return ResponseEntity.ok(pollService.answers(id))
    }

    @PostMapping
    fun create(
            @RequestBody body: PollDTO
    ) : ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        result["id"] = pollService.create(body)

        return ResponseEntity.ok(result)
    }

    @PostMapping("/answer")
    fun answer(
            @RequestBody body: AnswerDTO
    ) : ResponseEntity<Any> {
        pollService.answer(body)
        return ResponseEntity.noContent().build()
    }

    @PutMapping
    fun edit(
            @RequestBody body: PollDTO
    ) : ResponseEntity<Any> {
        pollService.edit(body)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}/publish")
    fun publish(
            @PathVariable id: String
    ) : ResponseEntity<Any> {
        pollService.publish(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}/changeAvailability")
    fun changeAvailability(
            @PathVariable id: String
    ) : ResponseEntity<Any> {
        pollService.changeAvailability(id)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun delete(
            @PathVariable id: String
    ) : ResponseEntity<Any> {
        pollService.delete(id)
        return ResponseEntity.noContent().build()
    }
}