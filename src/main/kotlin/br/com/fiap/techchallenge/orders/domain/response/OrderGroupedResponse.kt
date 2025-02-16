package br.com.fiap.techchallenge.orders.domain.response

data class OrderGroupedResponse(
    val ordersReady: List<OrderFollowUp>,
    val ordersPreparing: List<OrderFollowUp>,
    val ordersReceived: List<OrderFollowUp>
)
