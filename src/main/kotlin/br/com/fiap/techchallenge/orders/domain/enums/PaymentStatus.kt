package br.com.fiap.techchallenge.orders.domain.enums

enum class PaymentStatus(
        val title: String
) {
    APPROVED("APROVADO"),
    REPROVED("REPROVADO"),
    CANCELED("CANCELADO"),
    PENDING("PENDENTE")
}