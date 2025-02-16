package br.com.fiap.techchallenge.orders.mapper

import br.com.fiap.techchallenge.orders.domain.Orders
import br.com.fiap.techchallenge.orders.domain.enums.OrderStatus
import br.com.fiap.techchallenge.orders.domain.message.OrderConfirmedMessage
import br.com.fiap.techchallenge.orders.domain.message.OrderMessage
import br.com.fiap.techchallenge.orders.domain.request.OrderRequest
import br.com.fiap.techchallenge.orders.domain.request.OrderUpdateRequest
import br.com.fiap.techchallenge.orders.domain.response.OrderFollowUp
import br.com.fiap.techchallenge.orders.domain.response.OrderGroupedResponse
import br.com.fiap.techchallenge.orders.domain.response.OrderResponse
import org.springframework.stereotype.Component

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
            orderId = orders.orderNumber!!,
            orderValue = orders.totalValue!!
        )

    fun toOrderConfirmedMessage(orders: Orders) =
        OrderConfirmedMessage(
            orderId = orders.orderNumber!!,
            status = orders.orderStatus!!,
            details = orders
        )
    fun toListOrderResponse(orders: List<Orders>) = orders.map { toOrderResponse(it) }


    fun toGroupedOrders(orders: List<Orders>): OrderGroupedResponse {
        val orderFollowUps = orders.map { toFollowUp(it) }

        val readyOrders =
            orderFollowUps
                .stream()
                .filter { it.orderStatus == OrderStatus.READY
                }
                .sorted(Comparator.comparing (OrderFollowUp ::orderDate)).toList()

        val preparingOrders =
            orderFollowUps
                .stream()
                .filter{ it.orderStatus == (OrderStatus.IN_PREPARATION)}
                .sorted(Comparator.comparing(OrderFollowUp ::orderDate)).toList()

        val receivedOrders =
            orderFollowUps
                .stream()
                .filter { it.orderStatus == OrderStatus.RECEIVED
                }
                .sorted(Comparator.comparing(OrderFollowUp::orderDate)).toList()

        return OrderGroupedResponse(readyOrders, preparingOrders, receivedOrders)
    }

    fun toFollowUp(orders: Orders) = OrderFollowUp(
        orderId = requireNotNull(orders.orderNumber) { "Order number is required" },
        orderStatus = requireNotNull(orders.orderStatus) { "Order status is required" },
        orderDate = orders.orderDate.toString()
    )
}