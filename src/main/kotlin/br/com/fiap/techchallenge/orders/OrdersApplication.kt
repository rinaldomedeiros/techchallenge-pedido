package br.com.fiap.techchallenge.orders

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableRabbit
class OrdersApplication

fun main(args: Array<String>) {
	runApplication<OrdersApplication>(*args)
}
