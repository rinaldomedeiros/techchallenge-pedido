package br.com.fiap.techchallenge.orders.domain.response

import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus

data class OrderFollowUp(
    val orderId : Int,
    val orderStatus : OrderStatus,
    val orderDate : String
) {
}