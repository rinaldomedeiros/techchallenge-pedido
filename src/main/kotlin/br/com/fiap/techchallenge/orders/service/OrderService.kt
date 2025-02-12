package br.com.fiap.techchallenge.orders.service

import br.com.fiap.techchallenge.orders.domain.Orders


interface OrderService {

    fun createOrder(orders: Orders): Orders
    fun findByOrderNumber(orderNumber: Int): Orders
    fun updateOrder(orders: Orders): Orders
}