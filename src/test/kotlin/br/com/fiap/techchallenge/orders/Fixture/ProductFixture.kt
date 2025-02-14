package br.com.fiap.techchallenge.orders.Fixture

import br.com.fiap.techchallenge.orders.domain.Customer
import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.Product
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus
import br.com.fiap.techchallenge.orders.domain.enums.ProductCategory
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

object ProductFixture {

    fun toProduct()=
        Product(
            name = "Big Burguer",
            description = " Hambuguer com duas carnes",
            price = BigDecimal(12.50),
            productCategory = ProductCategory.SNACK
        )

}