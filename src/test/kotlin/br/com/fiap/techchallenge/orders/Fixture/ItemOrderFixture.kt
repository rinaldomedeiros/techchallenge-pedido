package br.com.fiap.techchallenge.orders.Fixture


import br.com.fiap.techchallenge.orders.domain.ItemOrder
import java.math.BigDecimal

object ItemOrderFixture {
    fun toListItemOrder() : List<ItemOrder>{
        return listOf(
            ItemOrder(
                quantity = 2,
                unitPrice = BigDecimal(12.50),
                product = ProductFixture.toProduct(),

            )
        )
    }
}