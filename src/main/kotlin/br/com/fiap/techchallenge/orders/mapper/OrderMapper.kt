package br.com.fiap.techchallenge.orders.mapper

import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.request.OrderRequest
import br.com.fiap.techchallenge.orders.domain.response.OrderResponse
import org.springframework.stereotype.Component

@Component
class OrderMapper {

    fun toOrder(orderRequest: OrderRequest) : Orders{
        return Orders(
                items = orderRequest.items,
                customer = orderRequest.customer
        )
    }

    fun toOrderResponse(orders: Orders) =
            OrderResponse(
                    id = orders.id,
                    orderNumber = orders.orderNumber,
                    items = orders.items,
                    orderDate = orders.orderDate,
                    orderStatus = orders.orderStatus,
                    orderValue = orders.totalValue,
                    customer = orders.customer

            )

}