package br.com.fiap.techchallenge.orders.domain.response

import ItemOrder
import br.com.fiap.techchallenge.orders.domain.Customer
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderResponse(
    val id: String ?,
    val orderNumber : Int?,
    val orderDate : LocalDateTime?,
    val orderValue : BigDecimal?,
    val orderStatus : OrderStatus?,
    val items : List<ItemOrder>,
    val customer: Customer?,
    val paymentStatus: PaymentStatus?
)
