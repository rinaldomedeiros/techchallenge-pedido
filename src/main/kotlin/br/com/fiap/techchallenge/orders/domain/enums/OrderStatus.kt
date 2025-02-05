package br.com.fiap.techchallenge.orders.domain.enums

enum class OrderStatus(
        val title : String,
        val description : String
) {
    RECEIVED("RECEBIDO", "Pedido recebido"),
    IN_PREPARATION("EM PREPARAÇÃO", "Pedido em preparação"),
    READY("PRONTO", "Pedido está pronto"),
    FINISHED("FINALIZADO", "O pedido foi retirado pelo cliente")
}