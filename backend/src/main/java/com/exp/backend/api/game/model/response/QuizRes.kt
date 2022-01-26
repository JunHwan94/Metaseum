package com.exp.backend.api.game.model.response

import com.exp.backend.api.game.model.entity.FindDifference
import com.exp.backend.api.game.model.entity.HtgQuiz
import com.exp.backend.api.game.model.entity.OXQuiz
import com.exp.backend.api.game.model.entity.Quiz
import com.exp.backend.common.model.BaseResponseBody
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("퀴즈 조회 응답")
class QuizRes private constructor() : BaseResponseBody(){
    @ApiModelProperty("퀴즈 리스트")
    lateinit var quizMap: HashMap<Int, Any>

    companion object{
        fun <T: Quiz> of(statusCode: Int, message: String, quizzes: List<T>) =
            QuizRes().apply {
                this.statusCode = statusCode
                this.message = message
                this.quizMap = HashMap()
                quizzes.forEach{ quizMap[it.no] =
                    when{
                        quizzes[0] is HtgQuiz -> (it as HtgQuiz).imageUrl
                        quizzes[0] is OXQuiz -> (it as OXQuiz).question
                        else -> it as FindDifference
                    }
                }
            }
    }
}
