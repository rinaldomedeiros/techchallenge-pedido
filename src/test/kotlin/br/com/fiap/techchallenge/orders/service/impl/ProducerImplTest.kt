package br.com.fiap.techchallenge.orders.service.impl

import br.com.fiap.techchallenge.orders.config.RabbitMQConfig
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate

class OrderProducerTest {

    private val rabbitTemplate: RabbitTemplate = mockk(relaxed = true)
    private lateinit var orderProducer: ProducerImpl
    private val logger: Logger = LoggerFactory.getLogger(ProducerImpl::class.java)

    @BeforeEach
    fun setup() {
        orderProducer = ProducerImpl(rabbitTemplate, logger)
    }

    @Test
    fun `Deve enviar pedido para RabbitMQ`() {
        // Arrange
        val order = mapOf("orderNumber" to 123, "orderTotalValue" to 250.0)

        // Act
        orderProducer.sendOrder(order)

        // Assert
        verify {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_REQUESTED_EXCHANGE,
                RabbitMQConfig.ORDER_REQUESTED_ROUTING_KEY,
                order
            )
        }
    }

    @Test
    fun `Deve enviar confirmação de pedido para RabbitMQ`() {
        // Arrange
        val order = mapOf("orderNumber" to 456, "orderTotalValue" to 500.0)

        // Act
        orderProducer.sendOrderConfirmed(order)

        // Assert
        verify {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.CONFIRMED_ORDER_EXCHANGE,
                RabbitMQConfig.CONFIRMED_ORDER_ROUTING_KEY,
                order
            )
        }
    }
}
