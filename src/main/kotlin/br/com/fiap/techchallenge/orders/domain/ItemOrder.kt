package br.com.fiap.techchallenge.orders.domain

import java.math.BigDecimal

data class ItemOrder(
        val quantity: Int,
        val unitPrice: BigDecimal,
        val product: Product,
        val totalPrice: BigDecimal = unitPrice.multiply(quantity.toBigDecimal())
)
