package br.com.fiap.techchallenge.orders.domain.message

import br.com.fiap.techchallenge.orders.domain.Customer
import java.io.Serializable

data class OrderMessage(

    val orderNumber: Int,
    val totalValue: Double,

    ) : Serializable
