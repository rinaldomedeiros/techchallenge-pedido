/*
import br.com.fiap.techchallenge.orders.domain.Customer
import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.enums.PaymentStatus
import br.com.fiap.techchallenge.orders.repository.OrderRepository
import br.com.fiap.techchallenge.orders.service.impl.OrderServiceImpl
import br.com.fiap.techchallenge.orders.service.impl.SequenceGeneratorServiceImpl
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OrderServiceImplTest {

    private lateinit var orderService: OrderServiceImpl
    private val orderRepository: OrderRepository = mockk()
    private val sequenceGeneratorServiceImpl: SequenceGeneratorServiceImpl = mockk()

    @BeforeEach
    fun setup() {
        orderService = OrderServiceImpl(orderRepository, sequenceGeneratorServiceImpl)
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
}
*/
