package com.exp.backend.api.game.model.service

import com.exp.backend.api.game.model.response.QuizResult

interface GameService {
    fun <T> findAll(c: Class<T>): List<T>
    fun calculateHtgScore(email: String, answers: List<String>): Map<Int, QuizResult>
    fun calculateOXScore(email: String, answers: List<String>): Map<Int, QuizResult>
    fun addFindDifferenceTitle(email: String): Boolean
}
