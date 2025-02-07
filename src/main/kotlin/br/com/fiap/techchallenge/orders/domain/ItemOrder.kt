import br.com.fiap.techchallenge.orders.domain.Product
import java.math.BigDecimal

data class ItemOrder(
        val quantity: Int,
        val unitPrice: BigDecimal,
        val product: Product,
        val totalPrice: BigDecimal = unitPrice.multiply(quantity.toBigDecimal())
)
