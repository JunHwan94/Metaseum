package com.exp.backend.api.game.model.entity

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "heritageQuiz")
class HtgQuiz(no: Int, val imageUrl: String, val answer: Set<String>): Quiz(no)