package br.com.fiap.techchallenge.orders.Fixture

import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus
import br.com.fiap.techchallenge.orders.domain.request.OrderRequest
import br.com.fiap.techchallenge.orders.domain.response.OrderResponse
import java.math.BigDecimal
import java.time.LocalDateTime

object OrderFixture {
    val dateNow = LocalDateTime.now()

    fun toOrder()=
        Orders(
            id = "123",
            orderNumber = 123,
            orderStatus = OrderStatus.IN_PREPARATION,
            orderDate = dateNow,
            paymentStatus = PaymentStatus.PENDING,
            totalValue = BigDecimal(500),
            customer = CustomerFixture.toCustomer(),
            items = ItemOrderFixture.toListItemOrder()
        )

    fun toOrderRequest() =
        OrderRequest(
            items = ItemOrderFixture.toListItemOrder(),
            customer = CustomerFixture.toCustomer()
        )

}