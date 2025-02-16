package br.com.fiap.techchallenge.orders.repository

import br.com.fiap.techchallenge.orders.domain.Orders
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface OrderRepository : MongoRepository<Orders, Long> {
    fun findByOrderNumber(orderId: Int): Optional<Orders>
    fun findByOrderStatusAndOrderDateBefore(orderStatus: String, orderDate: Instant): List<Orders>

    @Query("{ 'orderstatus' : { \$ne: 'FINISHED' } }")
    fun findAllOrderSByDateCreation(): List<Orders>?


}