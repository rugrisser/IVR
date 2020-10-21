package org.hse.lycsovet

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Bean
    fun logger(): Logger {
        return LoggerFactory.getLogger(Config::class.java)
    }
}