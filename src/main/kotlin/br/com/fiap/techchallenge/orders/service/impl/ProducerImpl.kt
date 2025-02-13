package br.com.fiap.techchallenge.orders.service.impl

import br.com.fiap.techchallenge.orders.domain.message.OrderMessage
import com.example.orderproduction.config.RabbitMQConfig
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.Logger
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class ProducerImpl(
    private val rabbitTemplate: RabbitTemplate,
    private val logger: Logger = org.slf4j.LoggerFactory.getLogger(ProducerImpl::class.java)
) {

    fun sendOrder(order: Any)  {
        val jsonMessage = jacksonObjectMapper().writeValueAsString(order)
        rabbitTemplate.convertAndSend(RabbitMQConfig.PAID_ORDER_EXCHANGE, RabbitMQConfig.PAID_ORDER_ROUTING_KEY, jsonMessage)
        logger.info("Order sent to RabbitMQ: $order")

    }
}