package br.com.fiap.techchallenge.orders.service.impl

import br.com.fiap.techchallenge.orders.domain.DatabaseSequence
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

@Service
class SequenceGeneratorServiceImpl(
        private val mongoOperations : MongoOperations
) {
    fun generationSequence ( sequenceName : String): Long{
        val query = Query(Criteria.where("_.id").`is`(sequenceName))
        val update = Update().inc("seq",1)
        val options = FindAndModifyOptions.options().returnNew(true).upsert(true)

        val counter = mongoOperations.findAndModify(query,update,options,DatabaseSequence::class.java)
        return counter?.seq ?: 1
    }
}