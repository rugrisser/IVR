package org.hse.lycsovet.controller

import org.hse.lycsovet.Role
import org.hse.lycsovet.SetRoleDTO
import org.hse.lycsovet.User
import org.hse.lycsovet.service.UserServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/user")
class UserController(
        private val userService: UserServiceImpl
) {

    @GetMapping
    fun get(
            @RequestHeader("Authorization") token: String
    ) : ResponseEntity<User> {
        return ResponseEntity.ok(userService.getUser(token))
    }

    @GetMapping("/all")
    fun all(
            @RequestHeader("Authorization") token: String
    ) : ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.getAll(token))
    }

    @GetMapping("/{id}")
    fun get(
            @PathVariable id: Long,
            @RequestHeader("Authorization") token: String
    ) : ResponseEntity<User> {
        return ResponseEntity.ok(userService.getUser(token, id))
    }

    @GetMapping("/auth")
    fun auth(
            @RequestParam login: String,
            @RequestParam password: String
    ) : ResponseEntity<Map<String, String>> {
        val result = HashMap<String, String>()
        result["token"] = userService.login(login, password)

        return ResponseEntity.ok(result)
    }

    @GetMapping("/validate")
    fun validate(
            @RequestHeader("Authorization") token: String
    ) : ResponseEntity<Map<String, Any>> {
        val result = HashMap<String, Any>()
        result["result"] = userService.validate(token)

        return ResponseEntity.ok(result)
    }

    @GetMapping("/getRole")
    fun getRole(
            @RequestHeader("Authorization") token: String
    ) : ResponseEntity<Role> {
        return ResponseEntity.ok(userService.getRole(token))
    }

    @PatchMapping("/{id}/setRole")
    fun setRole(
            @PathVariable id: Long,
            @RequestHeader("Authorization") token: String,
            @RequestBody body: SetRoleDTO
    ) : ResponseEntity<Any> {
        userService.setRole(token, body.name, id)
        return ResponseEntity.noContent().build()
    }
}