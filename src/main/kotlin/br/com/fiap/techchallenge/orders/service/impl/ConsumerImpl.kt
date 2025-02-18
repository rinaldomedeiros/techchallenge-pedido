package br.com.fiap.techchallenge.orders.service.impl

import br.com.fiap.techchallenge.orders.config.RabbitMQConfig
import br.com.fiap.techchallenge.orders.domain.message.OrderStatusMessage
import br.com.fiap.techchallenge.orders.domain.message.OrderUpdatePaymentStatus
import br.com.fiap.techchallenge.orders.service.OrderService
import org.slf4j.Logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class ConsumerImpl(
    private val logger: Logger = org.slf4j.LoggerFactory.getLogger(ConsumerImpl::class.java),
    private val orderService : OrderService
) {
    @RabbitListener(queues = [RabbitMQConfig.ORDER_PAID_QUEUE])
    fun consumerUpdatedPaymentStatus(message : OrderUpdatePaymentStatus) {
        logger.info("Received message: $message")
        orderService.updatePaymentStatus(message.orderId, message.paymentStatus.toString())
    }

    @RabbitListener(queues = [RabbitMQConfig.UPDATED_ORDER_QUEUE])
    fun consumerUpdatedOrderStatus(message : OrderStatusMessage) {
        logger.info("Received message: $message")
        orderService.updateOrderStatus(message.orderId, message.orderStatus.toString())
    }
}