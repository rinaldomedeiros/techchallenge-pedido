package br.com.fiap.techchallenge.orders.domain

import br.com.fiap.techchallenge.orders.domain.enums.ProductCategory
import java.math.BigDecimal

data class Product(

        val name : String,
        val description : String,
        val price : BigDecimal,
        val productCategory : ProductCategory

)
