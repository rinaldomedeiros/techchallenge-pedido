package br.com.fiap.techchallenge.orders.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "counters")
data class DatabaseSequence(
        @Id
        val id: String = "orderSequence",
        val seq : Long
)
