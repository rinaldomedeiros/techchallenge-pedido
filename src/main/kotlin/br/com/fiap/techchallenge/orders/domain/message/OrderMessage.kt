package br.com.fiap.techchallenge.orders.domain.message

import java.io.Serializable
import java.math.BigDecimal

data class OrderMessage(
    val orderId: Int,
    val orderValue: BigDecimal,
) : Serializable
