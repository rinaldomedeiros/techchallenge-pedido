package br.com.fiap.techchallenge.orders.repository

import br.com.fiap.techchallenge.orders.domain.Orders
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.Optional

@Repository
interface OrderRepository : MongoRepository<Orders, Long> {
    fun findByOrderNumber(orderNumber: Int): Optional<Orders>
    fun findByOrderStatusAndOrderDateBefore(orderStatus: String, orderDate: Instant): List<Orders>
}