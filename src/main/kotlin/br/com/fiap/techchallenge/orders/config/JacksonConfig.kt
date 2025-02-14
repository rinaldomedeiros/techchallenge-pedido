package br.com.fiap.techchallenge.orders.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper() : ObjectMapper{
        return ObjectMapper()
            .registerModules(KotlinModule.Builder().build())
            .registerModules(JavaTimeModule())
    }
}