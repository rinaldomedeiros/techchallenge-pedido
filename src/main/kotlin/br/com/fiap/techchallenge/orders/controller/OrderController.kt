package br.com.fiap.techchallenge.orders.controller

import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.service.OrderService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class OrderController(
        private val orderService: OrderService
) {

    @PostMapping
    fun create(@RequestBody @Validated orders : Orders): Orders{
        return orderService.createOrder(orders)
    }
}