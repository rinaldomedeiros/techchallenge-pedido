package br.com.fiap.techchallenge.orders.service.impl

import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus
import br.com.fiap.techchallenge.orders.repository.OrderRepository
import br.com.fiap.techchallenge.orders.service.OrderService
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl(
        private val orderRepository: OrderRepository,
        private val sequenceGeneratorServiceImpl: SequenceGeneratorServiceImpl
) : OrderService {
    override fun createOrder(orders: Orders) : Orders {
        val orderSave = orders.copy(
                orderStatus = OrderStatus.IN_PREPARATION,
                paymentStatus = PaymentStatus.PENDING,
                orderNumber = orders.orderNumber ?:
                sequenceGeneratorServiceImpl.generationSequence("orderSequence").toInt()
                )
        return orderRepository.save(orderSave)
    }
}