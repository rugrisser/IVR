package org.hse.lycsovet.controller

import org.hse.lycsovet.Appeal
import org.hse.lycsovet.AppealDTO
import org.hse.lycsovet.ChangeAppealStatusDTO
import org.hse.lycsovet.service.AppealServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/appeal")
class AppealController(
    private val appealServiceImpl: AppealServiceImpl
) {
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long) : ResponseEntity<Appeal> {
        return ResponseEntity.ok(appealServiceImpl.get(id))
    }

    @GetMapping("/all")
    fun all() : ResponseEntity<List<Appeal>> {
        return ResponseEntity.ok(appealServiceImpl.all())
    }

    @GetMapping("/own")
    fun own(
            @RequestHeader("Authorization") token: String
    ) : ResponseEntity<List<Appeal>> {
        return ResponseEntity.ok(appealServiceImpl.own(token))
    }

    @GetMapping
    fun feed() : ResponseEntity<List<Appeal>> {
        return ResponseEntity.ok(appealServiceImpl.feed())
    }

    @PostMapping
    fun create(
            @RequestHeader("Authorization") token: String,
            @RequestBody appealDTO: AppealDTO
    ) : ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        result["id"] = appealServiceImpl.create(token, appealDTO)

        return ResponseEntity.ok(result)
    }

    @PutMapping
    fun edit(
            @RequestHeader("Authorization") token: String,
            @RequestBody appealDTO: AppealDTO
    ) : ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        appealServiceImpl.edit(token, appealDTO)
        result["status"] = "ok"

        return ResponseEntity.ok(result)
    }

    @PutMapping("/changeStatus")
    fun changeStatus(
            @RequestHeader("Authorization") token: String,
            @RequestBody body: ChangeAppealStatusDTO
    ) : ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        appealServiceImpl.changeStatus(token, body.id, body.status)
        result["status"] = "ok"

        return ResponseEntity.ok(result)
    }
}