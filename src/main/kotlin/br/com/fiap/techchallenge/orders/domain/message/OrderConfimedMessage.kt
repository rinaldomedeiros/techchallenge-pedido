package br.com.fiap.techchallenge.orders.domain.message

import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import java.io.Serializable

data class OrderConfirmedMessage(
    val orderNumber: Int,
    val status: OrderStatus,
    val details: Orders
) : Serializable