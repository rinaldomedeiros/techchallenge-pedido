package br.com.fiap.techchallenge.orders.domain.enums

enum class ProductCategory(
        val title : String
) {
    SNACK("LANCHE"),
    ACCOMPANIMENT("ACOMPANHAMENTO"),
    DRINK("BEBIDA"),
    DESSERT("SOBREMESA")
}