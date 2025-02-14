package br.com.fiap.techchallenge.orders

import br.com.fiap.techchallenge.orders.domain.request.OrderRequest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableRabbit
class OrdersApplication

fun main(args: Array<String>) {
    runApplication<OrdersApplication>(*args)

    val json = """{
  "items": [
    {
      "quantity": 10,
      "unitPrice": 25.12,
      "product": {
        "name": "Produto A",
        "description": "PRD",
        "price": 25.12,
        "productCategory" : "SNACK"
      }
    },
    {
      "quantity": 1,
      "unitPrice": 25.12,
      "product": {
        "name": "Produto B",
        "description": "PRD",
        "price": 25.12,
        "productCategory" : "SNACK"
      }
    }
  ],
  "customer": null
}
"""

    val objectMapper = jacksonObjectMapper()
    val order = objectMapper.readValue(json, OrderRequest::class.java)

    println(order)
}
