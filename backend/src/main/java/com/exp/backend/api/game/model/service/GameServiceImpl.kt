package com.exp.backend.api.game.model.service

import com.exp.backend.api.game.model.entity.HtgQuiz
import com.exp.backend.api.game.model.entity.OXQuiz
import com.exp.backend.api.game.model.response.QuizResult
import com.exp.backend.api.user.model.service.UserService
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service

@Service
class GameServiceImpl constructor(private val mongoTemplate: MongoTemplate, private val userService: UserService): GameService{
    private val HQ_TITLE = "좌의정"
    private val OX_TITLE = "영의정"
    private val FD_TITLE = "우의정"

    override fun <T> findAll(c: Class<T>): List<T> {
        return mongoTemplate.findAll(c)
    }

    override fun calculateHtgScore(email: String, answers: List<String>): Map<Int, QuizResult>{
        val resultMap = HashMap<Int, QuizResult>()
        var score = 0
        findAll(HtgQuiz::class.java).forEach{
            var right = false
            val userAnswer = answers[it.no - 1].replace(" ", "")
            if(it.answer.contains(userAnswer)) {
                right = true
                score++
            }
            resultMap[it.no] = QuizResult(if(right) 1 else 0, it.answer.first())
        }
        if(score == 5) userService.addUserTitle(email, HQ_TITLE)
        return resultMap
    }

    override fun calculateOXScore(email: String, answers: List<String>): Map<Int, QuizResult> {
        val resultMap = HashMap<Int, QuizResult>()
        var score = 0
        findAll(OXQuiz::class.java).forEach{
            var right = false
            val userAnswer = answers[it.no - 1].replace(" ", "")
            if(it.answer == userAnswer) {
                right = true
                score++
            }
            resultMap[it.no] = QuizResult(if(right) 1 else 0, it.answer)
        }
        if(score == 5) userService.addUserTitle(email, OX_TITLE)
        return resultMap
    }

    override fun addFindDifferenceTitle(email: String): Boolean {
        return userService.addUserTitle(email, FD_TITLE)
    }
}