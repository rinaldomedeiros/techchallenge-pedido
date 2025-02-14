package br.com.fiap.techchallenge.orders.config

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Bean
    fun orderPaidQueue(): Queue = QueueBuilder.durable(ORDER_PAID_QUEUE).build()

    @Bean
    fun orderRequestedExchange(): TopicExchange = TopicExchange(ORDER_REQUESTED_EXCHANGE)

    @Bean
    fun orderRequestedQueue(): Queue = QueueBuilder.durable(ORDER_REQUESTED_QUEUE).build()

    @Bean
    fun bindingPaidOrder(orderRequestedQueue: Queue, orderRequestedExchange: TopicExchange): Binding =
        BindingBuilder.bind(orderRequestedQueue).to(orderRequestedExchange).with(ORDER_REQUESTED_ROUTING_KEY)

    @Bean
    fun updatedOrderQueue(): Queue = QueueBuilder.durable(UPDATED_ORDER_QUEUE).build()


    @Bean
    fun confirmedOrderQueue(): Queue = QueueBuilder.durable(CONFIRMED_ORDER_QUEUE).build()

    @Bean
    fun confirmedOrderExchange(): TopicExchange = TopicExchange(CONFIRMED_ORDER_EXCHANGE)

    @Bean
    fun bindingConfirmedOrder(confirmedOrderQueue: Queue, confirmedOrderExchange: TopicExchange): Binding =
        BindingBuilder.bind(confirmedOrderQueue).to(confirmedOrderExchange).with(CONFIRMED_ORDER_ROUTING_KEY)

    @Bean
    fun jsonMessageConverter(): Jackson2JsonMessageConverter = Jackson2JsonMessageConverter()

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate =
        RabbitTemplate(connectionFactory).apply {
            messageConverter = jsonMessageConverter()
        }

    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin =
        RabbitAdmin(connectionFactory).apply { isAutoStartup = true }

    companion object {
        // Fila e exchange para pedidos pagos
        const val ORDER_REQUESTED_QUEUE: String = "ORDER_REQUESTED_QUEUE"
        const val ORDER_REQUESTED_EXCHANGE: String = "order-requested-exchange"
        const val ORDER_REQUESTED_ROUTING_KEY: String = "order.requested"

        // Fila para pedidos pagos
        const val ORDER_PAID_QUEUE: String = "ORDER_PAID_QUEUE"


        // Fila e exchange para pedidos pagos
        const val CONFIRMED_ORDER_QUEUE: String = "confirmed.order.queue"
        const val CONFIRMED_ORDER_EXCHANGE: String = "confirmed.order.exchange"
        const val CONFIRMED_ORDER_ROUTING_KEY: String = "confirmed.order"

        // Fila e exchange para atualizações de pedidos
        const val UPDATED_ORDER_QUEUE = "updated.order.queue"

    }
}
