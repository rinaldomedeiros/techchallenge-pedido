
import br.com.fiap.techchallenge.orders.domain.Customer
import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus
import br.com.fiap.techchallenge.orders.exceptions.OrdersExceptions
import br.com.fiap.techchallenge.orders.mapper.OrderMapper
import br.com.fiap.techchallenge.orders.repository.OrderRepository
import br.com.fiap.techchallenge.orders.service.impl.OrderServiceImpl
import br.com.fiap.techchallenge.orders.service.impl.ProducerImpl
import br.com.fiap.techchallenge.orders.service.impl.SequenceGeneratorServiceImpl
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.Logger
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class OrderServiceImplTest {

    private lateinit var orderService: OrderServiceImpl
    private val orderRepository: OrderRepository = mockk()
    private val sequenceGeneratorServiceImpl: SequenceGeneratorServiceImpl = mockk()
    private val orderMapper : OrderMapper = mockk()
    private val logger = mockk<Logger>()
    private val producerImpl = mockk<ProducerImpl>()


    @BeforeEach
    fun setup() {
        orderService = OrderServiceImpl(orderRepository, sequenceGeneratorServiceImpl, producerImpl,orderMapper, logger)
    }

    @Test
    fun `createOrder deve salvar o pedido com status e pagamento padrão`() {
        // Arrange
        val orders = Orders(
            id = "1",
            orderNumber = null, // Simula um novo pedido sem número gerado
            items = listOf(),
            totalValue = BigDecimal.ZERO,
            orderStatus = OrderStatus.PENDING_PAYMENT,
            paymentStatus = PaymentStatus.PENDING,
            customer = Customer(
                name = "Wilson",
                document = "123456789"
            )
        )

        val generatedOrderNumber = 1001
        val expectedOrder = orders.copy(
            orderStatus = OrderStatus.PENDING_PAYMENT,
            paymentStatus = PaymentStatus.PENDING,
            orderNumber = generatedOrderNumber,
            totalValue = BigDecimal.ZERO
        )

        every { sequenceGeneratorServiceImpl.generationSequence("orderSequence") } returns generatedOrderNumber.toLong()
        every { orderMapper.toOrderMessage(any()) } returns mockk()
        every { producerImpl.sendOrder(any()) } returns Unit
        every { orderRepository.save(any()) } returns expectedOrder

        // Act
        val result = orderService.createOrder(orders)

        // Assert
        result.orderNumber shouldBe generatedOrderNumber
        result.orderStatus shouldBe OrderStatus.PENDING_PAYMENT
        result.paymentStatus shouldBe PaymentStatus.PENDING
        result.totalValue shouldBe BigDecimal.ZERO
        verify { sequenceGeneratorServiceImpl.generationSequence("orderSequence") }
        verify { orderRepository.save(expectedOrder) }
    }

    @Test
    fun `createOrder deve manter orderNumber se já estiver presente`() {
        // Arrange
        val existingOrderNumber = 2002
        val orders = Orders(
            id = "2",
            orderNumber = existingOrderNumber,
            items = listOf(),
            totalValue = BigDecimal.ZERO,
            orderStatus = OrderStatus.PENDING_PAYMENT,
            paymentStatus = PaymentStatus.PENDING,
            customer = Customer(
                name = "Wilson",
                document = "123456789"
            )
        )

        val expectedOrder = orders.copy(
            orderStatus = OrderStatus.PENDING_PAYMENT,
            paymentStatus = PaymentStatus.PENDING,
            totalValue = BigDecimal.ZERO
        )

        every { orderMapper.toOrderMessage(any()) } returns mockk()
        every { producerImpl.sendOrder(any()) } returns Unit
        every { orderRepository.save(any()) } returns expectedOrder

        // Act
        val result = orderService.createOrder(orders)

        // Assert
        result.orderNumber shouldBe existingOrderNumber
        result.orderStatus shouldBe OrderStatus.PENDING_PAYMENT
        result.paymentStatus shouldBe PaymentStatus.PENDING
        result.totalValue shouldBe BigDecimal.ZERO
        verify(exactly = 0) { sequenceGeneratorServiceImpl.generationSequence(any()) }
        verify { orderRepository.save(expectedOrder) }
    }

    @Test
    fun `Deve retornar a ordem quando encontrar pelo número do pedido`() {
        // Arrange
        val orderId = 1
        val expectedOrder = Orders(
            id = "1",
            orderNumber = orderId,
            items = listOf(),
            totalValue = BigDecimal.ZERO,
            orderStatus = OrderStatus.PENDING_PAYMENT,
            paymentStatus = PaymentStatus.PENDING,
            customer = Customer(
                name = "Wilson",
                document = "123456789"
            )
        )

        every { orderRepository.findByOrderNumber(orderId) } returns Optional.of(expectedOrder)

        // Act
        val result = orderService.findByOrderNumber(orderId)

        // Assert
        assertEquals(expectedOrder, result)
        verify { orderRepository.findByOrderNumber(orderId) }
    }

    @Test
    fun `Deve lançar OrderNotFoundException quando não encontrar o pedido`() {
        // Arrange
        val orderId = 999

        every { orderRepository.findByOrderNumber(orderId) } returns Optional.empty()

        // Act & Assert
        val exception = assertThrows(OrdersExceptions.OrderNotFound::class.java) {
            orderService.findByOrderNumber(orderId)
        }
        assertEquals("Order not found", exception.message)

        verify { orderRepository.findByOrderNumber(orderId) }
    }

    @Test
    fun `Deve atualizar um pedido com sucesso`() {
        // Arrange
        val order = Orders(orderNumber = 123, orderStatus = OrderStatus.PENDING_PAYMENT, paymentStatus = PaymentStatus.PENDING, items = listOf())
        val updatedOrder = Orders(orderNumber = 123, orderStatus = OrderStatus.RECEIVED, paymentStatus = PaymentStatus.APPROVED, items = listOf())

        mockkObject(orderService) // Permite mockar métodos internos da classe
        every { orderService.validateStatus(order) } returns updatedOrder
        every { orderRepository.save(updatedOrder) } returns updatedOrder

        // Act
        val result = orderService.updateOrder(order)

        // Assert
        assertEquals(updatedOrder, result)
        verify { orderService.validateStatus(order) }
        verify { orderRepository.save(updatedOrder) }
    }

    @Test
    fun `Deve cancelar pedido quando o pagamento for reprovado`() {
        // Arrange
        val order = Orders(orderNumber = 1, paymentStatus = PaymentStatus.REPROVED, orderStatus = OrderStatus.PENDING_PAYMENT, items = listOf())
        val expectedOrder = order.copy(orderStatus = OrderStatus.CANCELED)

        // Act
        val result = orderService.validateStatus(order)

        // Assert
        assertEquals(expectedOrder, result)
    }

    @Test
    fun `Deve marcar pedido como RECEIVED quando pagamento for aprovado`() {
        // Arrange
        val order = Orders(orderNumber = 2, paymentStatus = PaymentStatus.APPROVED, orderStatus = OrderStatus.PENDING_PAYMENT, items = listOf())
        val expectedOrder = order.copy(orderStatus = OrderStatus.RECEIVED)

        // Act
        val result = orderService.validateStatus(order)

        // Assert
        assertEquals(expectedOrder, result)
    }

    @Test
    fun `Deve manter o status do pedido se o pagamento estiver pendente`() {
        // Arrange
        val order = Orders(orderNumber = 3, paymentStatus = PaymentStatus.PENDING, orderStatus = OrderStatus.PENDING_PAYMENT, items = listOf())

        // Act
        val result = orderService.validateStatus(order)

        // Assert
        assertEquals(order, result) // O status deve permanecer o mesmo
    }

    @Test
    fun `Deve atualizar o status do pedido com sucesso`() {
        // Arrange
        val orderId = 1
        val newStatus = "RECEIVED"
        val existingOrder = Orders(orderNumber = orderId, orderStatus = OrderStatus.PENDING_PAYMENT, items = listOf())
        val updatedOrder = existingOrder.copy(orderStatus = OrderStatus.RECEIVED)

        every { orderService.findByOrderNumber(orderId) } returns existingOrder
        every { orderRepository.save(updatedOrder) } returns updatedOrder

        // Act
        val result = orderService.updateOrderStatus(orderId, newStatus)

        // Assert
        assertEquals(updatedOrder, result)
        verify { orderService.findByOrderNumber(orderId) }
        verify { orderRepository.save(updatedOrder) }
    }

    @Test
    fun `Deve lançar IllegalArgumentException se o status for inválido`() {
        // Arrange
        val orderId = 1
        val invalidStatus = "INVALID_STATUS"
        val existingOrder = Orders(orderNumber = orderId, orderStatus = OrderStatus.PENDING_PAYMENT, items = listOf())

        every { orderService.findByOrderNumber(orderId) } returns existingOrder

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            orderService.updateOrderStatus(orderId, invalidStatus)
        }

        assertTrue(exception.message!!.contains("No enum constant")) // Valida que a exceção ocorreu

        verify { orderService.findByOrderNumber(orderId) }
        verify(exactly = 0) { orderRepository.save(any()) } // Garante que `save()` não foi chamado
    }


    @Test
    fun `Não deve cancelar pedidos se não houver expirados`() {
        // Arrange
        every { orderRepository.findByOrderStatusAndOrderDateBefore(any(), any()) } returns emptyList()

        // Act
        orderService.canceledOrderExpired()

        // Assert
        verify { orderRepository.findByOrderStatusAndOrderDateBefore(OrderStatus.PENDING_PAYMENT.toString(), any()) }
        verify(exactly = 0) { orderRepository.save(any()) } // Garante que nada foi salvo
    }

    @Test
    fun `Deve retornar todos os pedidos`() {
        // Arrange
        val orders = listOf(
            Orders(orderNumber = 1, orderStatus = OrderStatus.RECEIVED, orderDate = LocalDateTime.now(), items = listOf()),
            Orders(orderNumber = 2, orderStatus = OrderStatus.IN_PREPARATION, orderDate = LocalDateTime.now(), items = listOf())
        )

        every { orderRepository.findAll() } returns orders

        // Act
        val result = orderService.getAll()

        // Assert
        assertEquals(orders, result)
        verify { orderRepository.findAll() }
    }

    @Test
    fun `Deve retornar lista vazia se não houver pedidos`() {
        // Arrange
        every { orderRepository.findAll() } returns emptyList()

        // Act
        val result = orderService.getAll()

        // Assert
        assertEquals(emptyList<Orders>(), result)
        verify { orderRepository.findAll() }
    }

    @Test
    fun `Deve retornar pedidos ordenados por data`() {
        // Arrange
        val orders = listOf(
            Orders(orderNumber = 1, orderStatus = OrderStatus.RECEIVED, orderDate = LocalDateTime.now(), items = listOf()),
            Orders(orderNumber = 2, orderStatus = OrderStatus.IN_PREPARATION, orderDate = LocalDateTime.now(), items = listOf())
        )

        every { orderRepository.findAllOrderSByDateCreation() } returns orders

        // Act
        val result = orderService.getFollowUp()

        // Assert
        assertEquals(orders, result)
        verify { orderRepository.findAllOrderSByDateCreation() }
    }

    @Test
    fun `Deve retornar lista vazia se findAllOrderSByDateCreation retornar null`() {
        // Arrange
        every { orderRepository.findAllOrderSByDateCreation() } returns null

        // Act
        val result = orderService.getFollowUp()

        // Assert
        assertEquals(emptyList<Orders>(), result)
        verify { orderRepository.findAllOrderSByDateCreation() }
    }
}
