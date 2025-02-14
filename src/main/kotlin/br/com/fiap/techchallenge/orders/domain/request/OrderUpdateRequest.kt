package br.com.fiap.techchallenge.orders.domain.request

import ItemOrder
import br.com.fiap.techchallenge.orders.domain.Customer
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus

data class OrderUpdateRequest(
    val paymentStatus: PaymentStatus
)
