package br.com.fiap.techchallenge.orders.mapper

import br.com.fiap.techchallenge.orders.Fixture.OrderFixture
import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.response.OrderResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest
class OrderMapperTest {
    @Autowired
    private lateinit var orderMapper: OrderMapper

    @Test
    fun `toOrder deve mapear OrderRequest para Orders corretamente`() {
        val orderRequest = OrderFixture.toOrderRequest()
        val expectedOrder = Orders(
            items = orderRequest.items,
            customer = orderRequest.customer
        )

        val result = orderMapper.toOrder(orderRequest)

        assertEquals(expectedOrder, result)
    }

    @Test
    fun `toOrderResponse deve mapear Orders para OrderResponse corretamente`() {
        // Arrange (Criando um objeto Orders de exemplo)
        val orders = OrderFixture.toOrder()

        // Act (Chamada do método a ser testado)
        val result: OrderResponse = orderMapper.toOrderResponse(orders)

        // Assert (Verificando se os valores foram corretamente mapeados)
        assertEquals(orders.id, result.id)
        assertEquals(orders.orderNumber, result.orderNumber)
        assertEquals(orders.items, result.items)
        assertEquals(orders.orderDate, result.orderDate)
        assertEquals(orders.orderStatus, result.orderStatus)
        assertEquals(orders.totalValue, result.orderValue)
        assertEquals(orders.customer, result.customer)
    }

    @Test
    fun `Deve mapear Orders para OrderFollowUp corretamente`() {
        val data = LocalDateTime.now()
        val order = Orders(
            orderNumber = 123,
            orderStatus = OrderStatus.RECEIVED,
            orderDate = data,
            items = emptyList()
        )

        val result = orderMapper.toFollowUp(order)

        assertEquals(123, result.orderId)
        assertEquals(OrderStatus.RECEIVED, result.orderStatus)

    }

    @Test
    fun `Deve lançar exceção quando orderNumber for nulo`() {
        val order = Orders(
            orderNumber = null,
            orderStatus = OrderStatus.RECEIVED,
            orderDate = LocalDateTime.now(),
            items = emptyList()
        )

        val exception = assertThrows<IllegalArgumentException> {
            orderMapper.toFollowUp(order)
        }

        assertEquals("Order number is required", exception.message)
    }

    @Test
    fun `Deve lançar exceção quando orderStatus for nulo`() {
        val order = Orders(
            orderNumber = 123,
            orderStatus = null,
            orderDate = LocalDateTime.now(),
            customer = null,
            items = emptyList()
        )

        val exception = assertThrows<IllegalArgumentException> {
            orderMapper.toFollowUp(order)
        }

        assertEquals("Order status is required", exception.message)
    }

    @Test
    fun `Deve agrupar pedidos corretamente`() {
        val orders = listOf(
            Orders(
                "123", orderNumber = 1, orderDate = LocalDateTime.of(2024, 2, 15, 12, 0, 0),
                BigDecimal.valueOf(10),OrderStatus.READY,  emptyList(), null),
            Orders(
                "123", orderNumber = 2, orderDate = LocalDateTime.of(2024, 2, 15, 12, 0, 0),
                BigDecimal.valueOf(10),OrderStatus.READY,  emptyList(), null),
            Orders(
                "123", orderNumber = 3, orderDate = LocalDateTime.of(2024, 2, 15, 12, 0, 0),
                BigDecimal.valueOf(10),OrderStatus.IN_PREPARATION,  emptyList(), null),
            Orders(
                "123", orderNumber = 4, orderDate = LocalDateTime.of(2024, 2, 15, 12, 0, 0),
                BigDecimal.valueOf(10),OrderStatus.RECEIVED,  emptyList(), null),

        )

        val result = orderMapper.toGroupedOrders(orders)

        assertEquals(1, result.ordersReceived.size)
        assertEquals(1, result.ordersPreparing.size)
        assertEquals(2, result.ordersReady.size)

        assertEquals(1, result.ordersReady.first().orderId)
        assertEquals(2, result.ordersReady.last().orderId)
    }

    @Test
    fun `Deve retornar listas vazias quando não há pedidos`() {
        val orders = emptyList<Orders>()

        val result = orderMapper.toGroupedOrders(orders)

        assertTrue(result.ordersReady.isEmpty())
        assertTrue(result.ordersPreparing.isEmpty())
        assertTrue(result.ordersReceived.isEmpty())
    }

}