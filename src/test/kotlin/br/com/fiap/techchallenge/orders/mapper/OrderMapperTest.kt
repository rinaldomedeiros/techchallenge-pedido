package br.com.fiap.techchallenge.orders.mapper

import br.com.fiap.techchallenge.orders.Fixture.OrderFixture
import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.response.OrderResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

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
}