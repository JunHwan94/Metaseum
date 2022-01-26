package com.exp.backend.api.guestboard.model.service

import com.exp.backend.api.guestboard.model.entity.GuestNote
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

@Service
class GuestBoardServiceImpl constructor(private val mongoTemplate: MongoTemplate) : GuestBoardService {
    private var updateNo = mongoTemplate.findAll(GuestNote::class.java).size

    override fun findAll(): List<GuestNote> {
        return mongoTemplate.findAll(GuestNote::class.java)
    }

    override fun upsertByNo(guestNoteUrl: String): Int {
        increaseUpdateIdx()
        if(mongoTemplate.upsert(
                        Query.query(Criteria.where("guestNoteNo").`is`(updateNo)),
                        Update().set("guestNoteUrl", guestNoteUrl),
                        GuestNote::class.java
                ).wasAcknowledged()) return updateNo
        return -1
    }

    private fun increaseUpdateIdx(){
        synchronized(updateNo){
            updateNo %= 8
            updateNo++
        }
    }
}