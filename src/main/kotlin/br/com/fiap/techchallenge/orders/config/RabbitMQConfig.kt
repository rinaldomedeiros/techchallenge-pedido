package com.example.orderproduction.config

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {
    @Bean
    fun paidOrderQueue(): Queue {
        return QueueBuilder.durable(PAID_ORDER_QUEUE).build()
    }

    @Bean
    fun paidOrderExchange(): TopicExchange {
        return TopicExchange(PAID_ORDER_EXCHANGE)
    }

    @Bean
    fun bindingPaidOrder(paidOrderQueue: Queue?, paidOrderExchange: TopicExchange?): Binding {
        return BindingBuilder.bind(paidOrderQueue).to(paidOrderExchange).with(PAID_ORDER_ROUTING_KEY)
    }

    @Bean
    fun updatedOrderQueue(): Queue {
        return QueueBuilder.durable(UPDATED_ORDER_QUEUE).build()
    }

    @Bean
    fun updatedOrderExchange(): TopicExchange {
        return TopicExchange(UPDATED_ORDER_EXCHANGE)
    }

    @Bean
    fun jsonMessageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun bindingUpdatedOrder(updatedOrderQueue: Queue?, updatedOrderExchange: TopicExchange?): Binding {
        return BindingBuilder.bind(updatedOrderQueue).to(updatedOrderExchange).with(UPDATED_ORDER_ROUTING_KEY)
    }


    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory?): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory!!)
        rabbitTemplate.messageConverter = jsonMessageConverter()
        return rabbitTemplate
    }

    companion object {
        // Fila e exchange para pedidos pagos
        const val PAID_ORDER_QUEUE: String = "paid.order.queue"
        const val PAID_ORDER_EXCHANGE: String = "paid.order.exchange"
        const val PAID_ORDER_ROUTING_KEY: String = "new.order"

        // Fila e exchange para atualizações de pedidos
        const val UPDATED_ORDER_QUEUE: String = "updated.order.queue"
        const val UPDATED_ORDER_EXCHANGE: String = "updated.order.exchange"
        const val UPDATED_ORDER_ROUTING_KEY: String = "updated.order"

        const val ORDER_REQUESTED_EXCHANGE = "order-requested-exchange";
        const val ORDER_REQUESTED_QUEUE = "ORDER_REQUESTED_QUEUE";
        const val ORDER_REQUESTED_ROUTING_KEY = "order.requested";

        const val ORDER_PAID_EXCHANGE = "order-paid-exchange";
        const val ORDER_PAID_QUEUE = "ORDER_PAID_QUEUE";
        const val ORDER_PAID_ROUTING_KEY = "order.paid";
    }
}