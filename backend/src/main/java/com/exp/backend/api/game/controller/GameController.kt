package com.exp.backend.api.game.controller

import com.exp.backend.api.game.model.entity.FindDifference
import com.exp.backend.api.game.model.entity.HtgQuiz
import com.exp.backend.api.game.model.entity.OXQuiz
import com.exp.backend.api.game.model.response.QuizAnswerRes
import com.exp.backend.api.game.model.response.QuizRes
import com.exp.backend.api.game.model.service.GameService
import com.exp.backend.common.auth.UserDetails
import com.exp.backend.common.model.BaseResponseBody
import io.swagger.annotations.*
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Api(tags = ["게임(퀴즈) API"])
@RestController
@RequestMapping("/game")
class GameController constructor(private val service: GameService) {

    @GetMapping("/heritage")
    @ApiOperation(value="문화재 퀴즈 조회", notes="문화재 퀴즈 전체 조회")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = QuizRes::class),
    )
    fun getHtgQuestions(): ResponseEntity<QuizRes>{
        return ResponseEntity.status(200).body(QuizRes.of(200, "성공", service.findAll(HtgQuiz::class.java)))
    }

    @PostMapping("/heritage")
    @ApiOperation(value="문화재 퀴즈 결과 조회", notes="사용자가 제출한 답안의 점수 조회")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = QuizAnswerRes::class),
        ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody::class)
    )
    fun checkUserHtgAnswer(@ApiIgnore authentication: Authentication?, @ApiParam("사용자가 입력한 정답") @RequestBody answers: List<String>): ResponseEntity<out BaseResponseBody>{
        if(authentication != null){
            val email = (authentication.details as UserDetails).user.email
            return ResponseEntity.status(200).body(QuizAnswerRes.of(200, "성공", service.calculateHtgScore(email, answers)))
        }
        return ResponseEntity.status(401).body(BaseResponseBody.of(401, "인증 실패"))
    }

    @GetMapping("/ox")
    @ApiOperation(value="OX 퀴즈 조회", notes="OX 퀴즈 전체 조회")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = QuizRes::class),
    )
    fun getOXQuestions(): ResponseEntity<QuizRes>{
        return ResponseEntity.status(200).body(QuizRes.of(200, "성공", service.findAll(OXQuiz::class.java)))
    }

    @PostMapping("/ox")
    @ApiOperation(value="OX 퀴즈 결과 조회", notes="사용자가 제출한 답안의 점수 조회")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = QuizAnswerRes::class),
        ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody::class)
    )
    fun checkUserOXAnswer(@ApiIgnore authentication: Authentication?, @ApiParam("사용자가 입력한 정답") @RequestBody answers: List<String>): ResponseEntity<out BaseResponseBody>{
        if(authentication != null){
            val email = (authentication.details as UserDetails).user.email
            return ResponseEntity.status(200).body(QuizAnswerRes.of(200, "성공", service.calculateOXScore(email, answers)))
        }
        return ResponseEntity.status(401).body(BaseResponseBody.of(401, "인증 실패"))
    }

    @GetMapping("/find-difference")
    @ApiOperation(value="틀린그림찾기 문제 조회", notes="틀린그림찾기 문제 전체 조회")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = QuizRes::class)
    )
    fun getFindDifferenceQuestions(): ResponseEntity<QuizRes>{
        return ResponseEntity.status(200).body(QuizRes.of(200, "성공", service.findAll(FindDifference::class.java)))
    }

    @PostMapping("/find-difference")
    @ApiOperation(value="틀린그림찾기 타이틀 추가", notes="틀린그림찾기 타이틀 추가")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = BaseResponseBody::class),
        ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody::class),
        ApiResponse(code = 500, message = "서버 에러", response = BaseResponseBody::class)
    )
    fun addFindDifferenceTitle(@ApiIgnore authentication: Authentication?): ResponseEntity<BaseResponseBody>{
        authentication?.let {
            val email = (authentication.details as UserDetails).user.email
            return if(service.addFindDifferenceTitle(email))
                ResponseEntity.status(200).body(BaseResponseBody.of(200, "성공"))
            else ResponseEntity.status(500).body(BaseResponseBody.of(500, "서버 에러"))
        }
        return ResponseEntity.status(401).body(BaseResponseBody.of(401, "인증 실패"))
    }
}