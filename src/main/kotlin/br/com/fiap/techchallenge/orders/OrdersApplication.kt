package br.com.fiap.techchallenge.orders

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
