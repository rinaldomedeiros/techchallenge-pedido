package br.com.fiap.techchallenge.orders.controller

import br.com.fiap.techchallenge.orders.domain.request.OrderRequest
import br.com.fiap.techchallenge.orders.domain.request.OrderUpdateRequest
import br.com.fiap.techchallenge.orders.domain.response.OrderGroupedResponse
import br.com.fiap.techchallenge.orders.domain.response.OrderResponse
import br.com.fiap.techchallenge.orders.exceptions.OrdersExceptions.OrderNotFound
import br.com.fiap.techchallenge.orders.mapper.OrderMapper
import br.com.fiap.techchallenge.orders.service.OrderService
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService,
    private val orderMapper: OrderMapper
) {
    @PostMapping
    fun create(@RequestBody @Validated orders: OrderRequest): OrderResponse {
        println("Recebido: $orders")
        return orderMapper.toOrderResponse(orderService.createOrder(orderMapper.toOrder(orders)))
    }

    @GetMapping("/{orderNumber}")
    fun getOrder(
        @Parameter(description = "Número do pedido", example = "123")
        @PathVariable orderNumber: Int
    ): ResponseEntity<OrderResponse> {
        return try {
            val order = orderService.findByOrderNumber(orderNumber)
            ResponseEntity.status(201).body(orderMapper.toOrderResponse(order))
        } catch (e: OrderNotFound) {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{orderNumber}")
    fun updateOrder(
        @Parameter(description = "Número do pedido", example = "123")
        @PathVariable orderNumber: Int,
        @RequestBody @Validated orderUpdateRequest: OrderUpdateRequest
    ): ResponseEntity<OrderResponse> {
        return try {
            val orderUpdate = orderService.findByOrderNumber(orderNumber)
            val order = orderService.updateOrder(orderMapper.toOrderUpdate(orderUpdateRequest, orderUpdate))
            ResponseEntity.ok(orderMapper.toOrderResponse(order))
        } catch (e: OrderNotFound) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/follow-up")
    fun getFollowUp(): ResponseEntity<OrderGroupedResponse> {
        return ResponseEntity.ok(orderMapper.toGroupedOrders(orderService.getFollowUp()))
    }

    @GetMapping("/all")
    fun getAll(): ResponseEntity<List<OrderResponse>> =
        ResponseEntity.ok(orderMapper.toListOrderResponse(orderService.getAll()))
}