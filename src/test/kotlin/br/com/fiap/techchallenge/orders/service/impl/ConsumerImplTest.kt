package br.com.fiap.techchallenge.orders.service.impl

import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus
import br.com.fiap.techchallenge.orders.domain.message.OrderStatusMessage
import br.com.fiap.techchallenge.orders.domain.message.OrderUpdatePaymentStatus
import br.com.fiap.techchallenge.orders.service.OrderService
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OrderConsumerTest {

    private val orderService: OrderService = mockk(relaxed = true)
    private lateinit var orderConsumer: ConsumerImpl
    private val logger: Logger = LoggerFactory.getLogger(ConsumerImpl::class.java)

    @BeforeEach
    fun setup() {
        orderConsumer = ConsumerImpl( logger, orderService)
    }


    @Test
    fun `Deve consumir mensagem e atualizar status do pedido`() {
        // Arrange
        val message = OrderStatusMessage(orderId = 2, orderStatus = OrderStatus.RECEIVED)

        // Simula o comportamento do método `updateOrderStatus`, sem `just Runs`
        every { orderService.updateOrderStatus(2, OrderStatus.RECEIVED.toString()) } returns mockk()

        // Act
        orderConsumer.consumerUpdatedOrderStatus(message)

        // Assert
        verify { orderService.updateOrderStatus(2, OrderStatus.RECEIVED.toString()) }
    }

    @Test
    fun `Deve consumir mensagem e atualizar status do pagamento`() {
        // Arrange
        val message = OrderUpdatePaymentStatus(orderId = 2, paymentStatus = PaymentStatus.APPROVED)

        // Simula o comportamento do método `updatePaymentStatus`, sem `just Runs`
        every { orderService.updatePaymentStatus(2, PaymentStatus.APPROVED.toString()) } returns mockk()

        // Act
        orderConsumer.consumerUpdatedPaymentStatus(message)

        // Assert
        verify { orderService.updatePaymentStatus(2, PaymentStatus.APPROVED.toString()) }
    }
}
