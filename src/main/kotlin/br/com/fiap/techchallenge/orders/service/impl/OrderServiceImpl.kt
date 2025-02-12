package br.com.fiap.techchallenge.orders.service.impl

import ItemOrder
import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus
import br.com.fiap.techchallenge.orders.exceptions.OrdersExceptions.OrderNotFound
import br.com.fiap.techchallenge.orders.repository.OrderRepository
import br.com.fiap.techchallenge.orders.service.OrderService
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val sequenceGeneratorServiceImpl: SequenceGeneratorServiceImpl
) : OrderService {
    override fun createOrder(orders: Orders): Orders {
        val orderSave = orders.copy(
            orderStatus = OrderStatus.PENDING_PAYMENT,
            paymentStatus = PaymentStatus.PENDING,
            orderNumber = orders.orderNumber ?: sequenceGeneratorServiceImpl.generationSequence("orderSequence")
                .toInt(),
            totalValue = sumValueTotal(orders.items)
        )
        return orderRepository.save(orderSave)
    }

    override fun findByOrderNumber(orderNumber: Int): Orders {
        return orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow { OrderNotFound("Order not found") }
    }

    override fun updateOrder(order: Orders): Orders {
        val orderPaymentStatus = validateStatus(order)
        return orderRepository.save(orderPaymentStatus)
    }

    private fun sumValueTotal(orderItems: List<ItemOrder>): BigDecimal {
        return orderItems.stream().map(ItemOrder::totalPrice).reduce(BigDecimal.ZERO, BigDecimal::add)
    }

    private fun validateStatus(orders: Orders): Orders {
        return when (orders.paymentStatus) {
            PaymentStatus.REPROVED -> orders.copy(orderStatus = OrderStatus.CANCELED)
            PaymentStatus.APPROVED -> orders.copy(orderStatus = OrderStatus.IN_PREPARATION)
            else -> orders
        }
    }
}