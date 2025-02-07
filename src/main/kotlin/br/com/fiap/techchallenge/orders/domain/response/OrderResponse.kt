package br.com.fiap.techchallenge.orders.domain.response

import ItemOrder
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderResponse(
    val id: String ?,
    val orderNumber : Int?,
    val orderDate : LocalDateTime?,
    val orderValue : BigDecimal?,
    val orderStatus : OrderStatus?,
    val items : List<ItemOrder>
)
