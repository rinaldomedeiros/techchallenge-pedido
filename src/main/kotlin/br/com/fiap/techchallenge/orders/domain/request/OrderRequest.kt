package br.com.fiap.techchallenge.orders.domain.request

import br.com.fiap.techchallenge.orders.domain.ItemOrder
import br.com.fiap.techchallenge.orders.domain.Customer

data class OrderRequest(
    val items: List<ItemOrder>,
    val customer: Customer?
)
