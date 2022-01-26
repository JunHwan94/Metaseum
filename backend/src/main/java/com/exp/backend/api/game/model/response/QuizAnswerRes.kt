package com.exp.backend.api.game.model.response

import com.exp.backend.common.model.BaseResponseBody
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("퀴즈 결과 반환")
class QuizAnswerRes private constructor() : BaseResponseBody(){
    @ApiModelProperty("획득한 점수")
    var score = 0
    @ApiModelProperty("문제별 정답 여부, 정답")
    lateinit var result: Map<Int, QuizResult>

    companion object{
        fun of(statusCode: Int, message: String, result: Map<Int, QuizResult>) =
            QuizAnswerRes().apply{
                this.statusCode = statusCode
                this.message = message
                this.result = result
                for(i in 1 .. 5) score += result[i]!!.right
            }
    }
}

class QuizResult(val right: Int, val answer: String)