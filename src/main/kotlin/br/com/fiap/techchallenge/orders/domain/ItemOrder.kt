package br.com.fiap.techchallenge.orders.domain

import java.math.BigDecimal

data class ItemOrder(
        val quantity: Int,
        val pricetotal: BigDecimal,
        val product: Product
)
