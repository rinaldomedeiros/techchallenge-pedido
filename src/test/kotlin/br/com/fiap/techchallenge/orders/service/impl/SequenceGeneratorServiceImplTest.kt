package br.com.fiap.techchallenge.orders.service.impl

import br.com.fiap.techchallenge.orders.domain.DatabaseSequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update

class SequenceGeneratorServiceTest {

    private val mongoOperations: MongoOperations = mockk()
    private val sequenceGeneratorService = SequenceGeneratorServiceImpl(mongoOperations)

    @Test
    fun `Deve retornar o próximo valor da sequência`() {
        // Arrange
        val sequenceName = "order_sequence"
        val query = Query(Criteria.where("_.id").`is`(sequenceName))
        val update = Update().inc("seq", 1)
        val options = FindAndModifyOptions.options().returnNew(true).upsert(true)

        val databaseSequence = DatabaseSequence(sequenceName, 100L)

        // Usar 'any' para corresponder com qualquer valor
        every {
            mongoOperations.findAndModify(
                any<Query>(), // Aceita qualquer Query
                any<Update>(), // Aceita qualquer Update
                any<FindAndModifyOptions>(), // Aceita qualquer FindAndModifyOptions
                eq(DatabaseSequence::class.java) // Aceita especificamente DatabaseSequence
            )
        } returns databaseSequence

        // Act
        val result = sequenceGeneratorService.generationSequence(sequenceName)

        // Assert
        assertEquals(100L, result)
        verify {
            mongoOperations.findAndModify(
                any<Query>(),
                any<Update>(),
                any<FindAndModifyOptions>(),
                eq(DatabaseSequence::class.java)
            )
        }
    }

    @Test
    fun `Deve retornar 1 quando a sequência não existir`() {
        // Arrange
        val sequenceName = "new_sequence"
        every {
            mongoOperations.findAndModify(any(), any(), any<FindAndModifyOptions>(), DatabaseSequence::class.java)
        } returns null

        // Act
        val result = sequenceGeneratorService.generationSequence(sequenceName)

        // Assert
        assertEquals(1L, result)
    }
}
