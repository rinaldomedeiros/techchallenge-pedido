package br.com.fiap.techchallenge.orders.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Bean
    fun paidOrderQueue(): Queue = QueueBuilder.durable(PAID_ORDER_QUEUE).build()

    @Bean
    fun paidOrderExchange(): TopicExchange = TopicExchange(PAID_ORDER_EXCHANGE)

    @Bean
    fun bindingPaidOrder(paidOrderQueue: Queue, paidOrderExchange: TopicExchange): Binding =
        BindingBuilder.bind(paidOrderQueue).to(paidOrderExchange).with(PAID_ORDER_ROUTING_KEY)

    @Bean
    fun updatedOrderQueue(): Queue = QueueBuilder.durable(UPDATED_ORDER_QUEUE).build()

    @Bean
    fun updatedOrderExchange(): TopicExchange = TopicExchange(UPDATED_ORDER_EXCHANGE)

    @Bean
    fun bindingUpdatedOrder(updatedOrderQueue: Queue, updatedOrderExchange: TopicExchange): Binding =
        BindingBuilder.bind(updatedOrderQueue).to(updatedOrderExchange).with(UPDATED_ORDER_ROUTING_KEY)

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
        const val PAID_ORDER_QUEUE = "paid.order.queue"
        const val PAID_ORDER_EXCHANGE = "paid.order.exchange"
        const val PAID_ORDER_ROUTING_KEY = "new.order"

        // Fila e exchange para atualizações de pedidos
        const val UPDATED_ORDER_QUEUE = "updated.order.queue"
        const val UPDATED_ORDER_EXCHANGE = "updated.order.exchange"
        const val UPDATED_ORDER_ROUTING_KEY = "updated.order"
    }
}
