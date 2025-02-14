package br.com.fiap.techchallenge.orders.domain.request

import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus

data class OrderUpdateRequest(
    val paymentStatus: PaymentStatus
)
