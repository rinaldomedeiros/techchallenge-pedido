package br.com.fiap.techchallenge.orders.Fixture

import br.com.fiap.techchallenge.orders.domain.Customer
import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus
import java.time.LocalDate
import java.time.LocalDateTime

object CustomerFixture {

    fun toCustomer()=
        Customer(
            name = "Wilson Santos",
            document = "78945612321"
        )

}