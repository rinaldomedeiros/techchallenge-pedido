package br.com.fiap.techchallenge.orders.domain.message

import br.com.fiap.techchallenge.orders.domain.Customer
import java.io.Serializable
import java.math.BigDecimal

data class OrderMessage(

    val orderId: Int,
    val orderValue: BigDecimal,

    ) : Serializable
