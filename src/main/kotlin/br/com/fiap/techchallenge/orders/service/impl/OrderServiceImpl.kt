package br.com.fiap.techchallenge.orders.service.impl

import ItemOrder
import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus
import br.com.fiap.techchallenge.orders.exceptions.OrdersExceptions.OrderNotFound
import br.com.fiap.techchallenge.orders.mapper.OrderMapper
import br.com.fiap.techchallenge.orders.repository.OrderRepository
import br.com.fiap.techchallenge.orders.service.OrderService
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val sequenceGeneratorServiceImpl: SequenceGeneratorServiceImpl,
    private val producerImpl: ProducerImpl,
    private val orderMapper: OrderMapper
) : OrderService {
    override fun createOrder(orders: Orders): Orders {
        val orderSave = orders.copy(
            orderStatus = OrderStatus.PENDING_PAYMENT,
            paymentStatus = PaymentStatus.PENDING,
            orderNumber = orders.orderNumber ?: sequenceGeneratorServiceImpl.generationSequence("orderSequence")
                .toInt(),
            totalValue = sumValueTotal(orders.items)
        )
        val orderSend = orderRepository.save(orderSave)
        producerImpl.sendOrder(orderMapper.toOrderMessage(orderSend))
        return orderSend
    }

    override fun findByOrderNumber(orderNumber: Int): Orders {
        return orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow { OrderNotFound("Order not found") }
    }

    override fun updateOrder(order: Orders): Orders {
        val orderPaymentStatus = validateStatus(order)
        return orderRepository.save(orderPaymentStatus)
    }


    override fun updatePaymentStatus(orderNumber: Int, status: String): Orders {
        val order = findByOrderNumber(orderNumber)
        val updateOrderStatus = when (PaymentStatus.valueOf(status)) {
            PaymentStatus.APPROVED -> updateOrderStatusApproved(order)
            PaymentStatus.REPROVED -> updateOrderStatusReproved(order)
            PaymentStatus.CANCELED -> updateOrderStatusCanceled(order)
            else -> order
        }
        return orderRepository.save(updateOrderStatus)
    }

    override fun updateOrderStatus(orderNumber: Int, status: String): Orders {
        val order = findByOrderNumber(orderNumber)
        val orderUpdate = order.copy(orderStatus = OrderStatus.valueOf(status))
        return orderRepository.save(orderUpdate)
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

    private fun updateOrderStatusApproved(order: Orders): Orders {
        val orderSend = order.copy(orderStatus = OrderStatus.IN_PREPARATION, paymentStatus = PaymentStatus.APPROVED)
        producerImpl.sendOrderConfirmed(orderMapper.toOrderConfirmedMessage(orderSend))
        return orderSend
    }

    private fun updateOrderStatusReproved(order: Orders): Orders {
        return order.copy(orderStatus = OrderStatus.PENDING_PAYMENT, paymentStatus = PaymentStatus.REPROVED)
    }

    private fun updateOrderStatusCanceled(order: Orders): Orders {
        return order.copy(orderStatus = OrderStatus.CANCELED, paymentStatus = PaymentStatus.CANCELED)
    }
}