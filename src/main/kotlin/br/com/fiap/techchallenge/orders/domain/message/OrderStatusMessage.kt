package br.com.fiap.techchallenge.orders.domain.message

import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus

data class OrderStatusMessage(
    val orderId: Int,
    val orderStatus: OrderStatus
)
