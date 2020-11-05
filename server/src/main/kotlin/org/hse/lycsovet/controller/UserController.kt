package org.hse.lycsovet.controller

import org.hse.lycsovet.service.UserServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin
@RestController
@RequestMapping("/user")
class UserController(
        private val userService: UserServiceImpl
) {

    @GetMapping("/auth")
    fun auth(
            @RequestParam login: String,
            @RequestParam password: String
    ) : ResponseEntity<Map<String, String>> {
        val result = HashMap<String, String>()
        result["token"] = userService.login(login, password)

        return ResponseEntity.ok(result)
    }
}