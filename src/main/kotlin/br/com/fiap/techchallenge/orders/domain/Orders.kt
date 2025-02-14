package br.com.fiap.techchallenge.orders.domain

import ItemOrder
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus
import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document(collection = "orders")
data class Orders(
        @Id
        val id : String? = null,

        @Indexed(unique = true)
        val orderNumber : Int? = null,
        @CreatedDate
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        val orderDate : LocalDateTime? = null,
        val totalValue : BigDecimal? = null,
        val orderStatus : OrderStatus? = null,
        val items: List<ItemOrder>,
        val paymentStatus : PaymentStatus? = null,
        val customer: Customer? = null
)
