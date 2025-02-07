package br.com.fiap.techchallenge.orders.repository

import br.com.fiap.techchallenge.orders.domain.Orders
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : MongoRepository<Orders, Long> {
}