package br.com.fiap.techchallenge.orders.service

import br.com.fiap.techchallenge.orders.domain.Orders


interface OrderService {

    fun createOrder(orders: Orders): Orders
    fun findByOrderNumber(orderNumber: Int): Orders
    fun updateOrder(orders: Orders): Orders
    fun updatePaymentStatus(orderNumber: Int, status: String): Orders
    fun updateOrderStatus(orderNumber: Int, status: String): Orders
    fun canceledOrderExpired()
}