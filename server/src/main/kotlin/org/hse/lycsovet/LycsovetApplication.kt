package org.hse.lycsovet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [ SecurityAutoConfiguration::class ])
class LycsovetApplication

fun main(args: Array<String>) {
    runApplication<LycsovetApplication>(*args)
}
