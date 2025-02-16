package br.com.fiap.techchallenge.orders.domain.message

import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus
import java.io.Serializable

data class OrderUpdatePaymentStatus(
    val orderId: Int,
    val paymentStatus: PaymentStatus,
) : Serializable
