package br.com.fiap.techchallenge.orders.service.impl

import br.com.fiap.techchallenge.orders.domain.ItemOrder
import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus
import br.com.fiap.techchallenge.orders.exceptions.OrdersExceptions.OrderNotFound
import br.com.fiap.techchallenge.orders.mapper.OrderMapper
import br.com.fiap.techchallenge.orders.repository.OrderRepository
import br.com.fiap.techchallenge.orders.service.OrderService
import org.slf4j.Logger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val sequenceGeneratorServiceImpl: SequenceGeneratorServiceImpl,
    private val producerImpl: ProducerImpl,
    private val orderMapper: OrderMapper,
    private val logger: Logger = org.slf4j.LoggerFactory.getLogger(OrderServiceImpl::class.java)
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

    override fun findByOrderNumber(orderId: Int): Orders {
        return orderRepository.findByOrderNumber(orderId)
            .orElseThrow { OrderNotFound("Order not found") }
    }

    override fun updateOrder(orders: Orders): Orders {
        val orderPaymentStatus = validateStatus(orders)
        return orderRepository.save(orderPaymentStatus)
    }


    override fun updatePaymentStatus(orderId: Int, status: String): Orders {
        val order = findByOrderNumber(orderId)
        val updateOrderStatus = when (PaymentStatus.valueOf(status)) {
            PaymentStatus.APPROVED -> updateOrderStatusApproved(order)
            PaymentStatus.REPROVED -> updateOrderStatusReproved(order)
            PaymentStatus.CANCELED -> updateOrderStatusCanceled(order)
            else -> order
        }
        return orderRepository.save(updateOrderStatus)
    }

    override fun updateOrderStatus(orderId: Int, status: String): Orders {
        val order = findByOrderNumber(orderId)
        val orderUpdate = order.copy(orderStatus = OrderStatus.valueOf(status))
        return orderRepository.save(orderUpdate)
    }

    @Scheduled(fixedRate = 60000)
    override fun canceledOrderExpired() {
        val orders = orderRepository.findByOrderStatusAndOrderDateBefore(
            OrderStatus.PENDING_PAYMENT.toString(),
            Instant.now().minusSeconds(300)
        )
        orders.forEach {
            val orderCanceled = it.copy(orderStatus = OrderStatus.CANCELED, paymentStatus = PaymentStatus.CANCELED)
            orderRepository.save(orderCanceled)
            logger.info("Order canceled by expiration: $orderCanceled")
        }
    }

    @Scheduled(fixedRate = 60000)
    override fun finishedOrder() {
        val orders = orderRepository.findByOrderStatusAndOrderDateBefore(
            OrderStatus.READY.toString(),
            Instant.now().minusSeconds(300)
        )
        orders.forEach {
            val orderFinished = it.copy(orderStatus = OrderStatus.FINISHED)
            orderRepository.save(orderFinished)
        }

    }

    override fun getAll(): List<Orders> {
        return orderRepository.findAll()
    }

    override fun getFollowUp(): List<Orders> {
        return orderRepository.findAllOrderSByDateCreation() ?: emptyList()
    }


    private fun sumValueTotal(orderItems: List<ItemOrder>): BigDecimal {
        return orderItems.stream().map(ItemOrder::totalPrice).reduce(BigDecimal.ZERO, BigDecimal::add)
    }

     fun validateStatus(orders: Orders): Orders {
        return when (orders.paymentStatus) {
            PaymentStatus.REPROVED -> orders.copy(orderStatus = OrderStatus.CANCELED)
            PaymentStatus.APPROVED -> orders.copy(orderStatus = OrderStatus.RECEIVED)
            else -> orders
        }
    }

    private fun updateOrderStatusApproved(order: Orders): Orders {
        val orderSend = order.copy(orderStatus = OrderStatus.RECEIVED, paymentStatus = PaymentStatus.APPROVED)
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