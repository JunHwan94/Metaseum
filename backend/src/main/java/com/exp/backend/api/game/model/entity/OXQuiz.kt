package com.exp.backend.api.game.model.entity

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "oxQuiz")
class OXQuiz(no: Int, val question: String, val answer: String): Quiz(no)