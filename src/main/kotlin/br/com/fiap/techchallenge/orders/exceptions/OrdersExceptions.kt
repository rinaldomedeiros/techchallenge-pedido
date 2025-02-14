package br.com.fiap.techchallenge.orders.exceptions

import java.lang.RuntimeException

object OrdersExceptions {

    class OrderNotFound(message : String) : RuntimeException(message)

}