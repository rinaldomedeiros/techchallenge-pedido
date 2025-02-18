package br.com.fiap.techchallenge.orders.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI().info(
            Info()
                .title("API de Pedidos")
                .version("v1.0")
                .description("API para gestão de pedidos")
                .contact(
                    Contact().name("Equipe de Desenvolvimento").email("dev@fiap.com")
                )
                .license(
                    License().name("MIT License").url("https://opensource.org/licenses/MIT")
                )
        )
    }
}