package br.com.fiap.techchallenge.orders.service

import br.com.fiap.techchallenge.orders.domain.Orders


interface OrderService {

    fun createOrder(orders: Orders): Orders
    fun findByOrderNumber(orderId: Int): Orders
    fun updateOrder(orders: Orders): Orders
    fun updatePaymentStatus(orderId: Int, status: String): Orders
    fun updateOrderStatus(orderId: Int, status: String): Orders
    fun canceledOrderExpired()
    fun finishedOrder()
    fun getAll(): List<Orders>
    fun getFollowUp(): List<Orders>

}