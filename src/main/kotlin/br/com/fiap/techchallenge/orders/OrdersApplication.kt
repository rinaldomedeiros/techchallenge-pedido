package br.com.fiap.techchallenge.orders

import br.com.fiap.techchallenge.orders.domain.request.OrderRequest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableRabbit
@EnableScheduling
class OrdersApplication

fun main(args: Array<String>) {
    runApplication<OrdersApplication>(*args)

}
