package br.com.fiap.techchallenge.orders.service.impl

import br.com.fiap.techchallenge.orders.domain.message.OrderMessage
import br.com.fiap.techchallenge.orders.config.RabbitMQConfig
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.Logger
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class ProducerImpl(
    private val rabbitTemplate: RabbitTemplate,
    private val logger: Logger = org.slf4j.LoggerFactory.getLogger(ProducerImpl::class.java),
    private val rabbitMQConfig: RabbitMQConfig
) {

    fun sendOrder(order: Any)  {
        val jsonMessage = jacksonObjectMapper().writeValueAsString(order)
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_REQUESTED_EXCHANGE, RabbitMQConfig.ORDER_REQUESTED_ROUTING_KEY, jsonMessage)
        logger.info("Order sent to RabbitMQ: $order")

    }

    fun sendOrderConfirmed(order: Any) {
        val jsonMessage = rabbitMQConfig.objectMapper().writeValueAsString(order)
        rabbitTemplate.convertAndSend(RabbitMQConfig.CONFIRMED_ORDER_EXCHANGE, RabbitMQConfig.CONFIRMED_ORDER_ROUTING_KEY, jsonMessage)
        logger.info("Order confirmed sent to RabbitMQ: $order")
    }
}