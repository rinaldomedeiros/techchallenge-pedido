package br.com.fiap.techchallenge.orders.mapper

import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.message.OrderMessage
import br.com.fiap.techchallenge.orders.domain.request.OrderRequest
import br.com.fiap.techchallenge.orders.domain.request.OrderUpdateRequest
import br.com.fiap.techchallenge.orders.domain.response.OrderResponse
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class OrderMapper {

    fun toOrder(orderRequest: OrderRequest): Orders {
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
            customer = orders.customer,
            paymentStatus = orders.paymentStatus

        )

    fun toOrderUpdate(orderUpdateRequest: OrderUpdateRequest, orders: Orders):Orders{
        return orders.copy(
            paymentStatus = orderUpdateRequest.paymentStatus
        )
    }

    fun toOrderMessage(orders: Orders) =
        OrderMessage(
            orderNumber = orders.orderNumber!!,
            totalValue = orders.totalValue!!.toDouble()
        )
}