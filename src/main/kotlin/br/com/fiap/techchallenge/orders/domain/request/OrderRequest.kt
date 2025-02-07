package br.com.fiap.techchallenge.orders.domain.request

import ItemOrder

data class OrderRequest(
    val items: List<ItemOrder>
)
