package com.exp.backend.api.game.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "findDifference")
class FindDifference(no: Int,
                     @JsonProperty("correct") val correctUrl: String,
                     @JsonProperty("incorrect") val incorrectUrl: String,
                     val name: String,
                     val artist: String,
                     val desc: String, 
                     @JsonProperty("ans") val locations: Set<List<Int>>
) : Quiz(no)