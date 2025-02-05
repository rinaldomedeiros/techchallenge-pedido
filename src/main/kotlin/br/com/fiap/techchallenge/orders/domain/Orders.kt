package br.com.fiap.techchallenge.orders.domain

import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document(collection = "orders")
data class Orders(
        @Id
        val id : Long?,
        val orderNumber : Int,
        @CreatedDate
        val orderDate : LocalDateTime?,
        val totalValue : BigDecimal,
        val orderStatus : OrderStatus,
        //Cliente cliente;
        val items: List<ItemOrder>,
        val paymentStatus : PaymentStatus
)
