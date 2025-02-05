package br.com.fiap.techchallenge.orders.service.impl

import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.repository.OrderRepository
import br.com.fiap.techchallenge.orders.service.OrderService
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl(
        private val orderRepository: OrderRepository
) : OrderService {
    override fun createOrder(orders: Orders) : Orders {
        return orderRepository.save(orders)
    }
}