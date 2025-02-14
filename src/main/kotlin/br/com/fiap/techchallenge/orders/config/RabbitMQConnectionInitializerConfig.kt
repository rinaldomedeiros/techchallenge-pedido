package br.com.fiap.techchallenge.orders.config

import jakarta.annotation.PostConstruct
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConnectionInitializerConfig(private val rabbitTemplate: RabbitTemplate) {

    @PostConstruct
    fun initRabbitConnection() {
        try {
            val connection = rabbitTemplate.connectionFactory.createConnection()
            println("Conexão com o RabbitMQ estabelecida: $connection")
        } catch (ex: Exception) {
            println("Erro ao estabelecer conexão com o RabbitMQ: $ex")
        }
    }
}
