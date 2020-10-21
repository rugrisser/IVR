package org.hse.lycsovet.controller

import org.hse.lycsovet.service.AppealServiceImpl
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/appeal")
class AppealController(
    private val appealServiceImpl: AppealServiceImpl
) {

}