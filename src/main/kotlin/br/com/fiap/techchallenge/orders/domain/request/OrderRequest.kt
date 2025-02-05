package br.com.fiap.techchallenge.orders.domain.request

import br.com.fiap.techchallenge.orders.domain.ItemOrder

data class OrderRequest(
    val items: List<ItemOrder>
)
