package com.exp.backend.api.heritage.model.service

import com.exp.backend.api.heritage.model.entity.Heritage
import lombok.AllArgsConstructor
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@AllArgsConstructor
@Service
class HeritageServiceImpl constructor(private val mongoTemplate: MongoTemplate) : HeritageService{

    override fun findAll(): List<Heritage> = mongoTemplate.findAll(Heritage::class.java)

    override fun findByCode(code: String): Heritage? =
        mongoTemplate.findOne(
            Query.query(Criteria.where("code").`is`(code)),
            Heritage::class.java
        )
}
