package br.com.fiap.techchallenge.orders.domain.response

import br.com.fiap.techchallenge.orders.domain.ItemOrder
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderResponse(
    val id: Long,
    val numberOrder : Int,
    val orderDate : LocalDateTime,
    val orderValue : BigDecimal,
    val orderStatus : OrderStatus,
    val items : List<ItemOrder>
)
