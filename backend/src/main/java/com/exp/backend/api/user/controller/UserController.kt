package com.exp.backend.api.user.controller

import com.exp.backend.api.user.model.entity.User
import com.exp.backend.api.user.model.request.UserReq
import com.exp.backend.api.user.model.response.StampRes
import com.exp.backend.api.user.model.response.UserRes
import com.exp.backend.api.user.model.service.UserService
import com.exp.backend.common.auth.JwtUtil
import com.exp.backend.common.auth.UserDetails
import com.exp.backend.common.model.BaseResponseBody
import io.swagger.annotations.*
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Api(value = "사용자 API", tags = ["사용자 API"])
@RestController
@RequestMapping("/user")
class UserController constructor(private val service: UserService) {

    @ApiOperation(value="구글 로그인", notes="구글 계정으로 로그인한다")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = UserRes::class),
        ApiResponse(code = 401, message = "인증실패", response = BaseResponseBody::class)
    )
    @PostMapping("/login/google")
    fun googleLogin(@ApiParam(value = "토큰", required = true, example = "{ \"id_token\" : \"토큰\" }") @RequestBody(required = true) data: Map<String, String>): ResponseEntity<out BaseResponseBody>{
        var message = "성공"
        val idTokenString = data["id_token"]!!
        val createResult = service.checkUser(idTokenString)
        if(!(createResult["created"] as Boolean)) message += " 이미 등록된 사용자"
        if(!(createResult["verified"] as Boolean)){
            return ResponseEntity.status(401).body(BaseResponseBody.of(401, "구글 로그인 실패"))
        }
        val user = createResult["user"] as User

        return ResponseEntity.status(200).body(UserRes.of(200, message, user, idTokenString.replace(JwtUtil.TOKEN_PREFIX, "")))
    }

    @ApiOperation(value="사용자 정보 조회", notes="사용자 정보를 조회한다")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = UserRes::class),
        ApiResponse(code = 401, message = "인증실패", response = BaseResponseBody::class),
        ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody::class)
    )
    @GetMapping("/info")
    fun getUserInfo(@ApiIgnore authentication: Authentication?): ResponseEntity<out BaseResponseBody>{
        authentication?.let{
            val email = (it.details as UserDetails).user.email
            service.findByEmail(email)?.let { user ->
                return ResponseEntity.status(200).body(UserRes.of(200, "성공", user))
            }
            return ResponseEntity.status(404).body(BaseResponseBody.of(404, "사용자 없음"))
        }
        return ResponseEntity.status(401).body(BaseResponseBody.of(401, "인증 실패"))
    }

    @ApiOperation(value="사용자 정보 수정", notes="사용자 정보를 수정한다")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = UserRes::class),
        ApiResponse(code = 401, message = "인증실패", response = BaseResponseBody::class),
        ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody::class)
    )
    @PatchMapping("/info")
    fun updateUserInfo(@ApiIgnore authentication: Authentication?, @RequestBody userReq: UserReq): ResponseEntity<out BaseResponseBody>{
        authentication?.let{
            val email = (it.details as UserDetails).user.email
            service.updateUserInfo(email, userReq)?.let { user ->
                return ResponseEntity.status(200).body(UserRes.of(200, "성공", user))
            }
            return ResponseEntity.status(404).body(BaseResponseBody.of(404, "사용자 없음"))
        }
        return ResponseEntity.status(401).body(BaseResponseBody.of(401, "인증 실패"))
    }

    @ApiOperation(value="스탬프 조회", notes="사용자의 스탬프를 조회한다")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = StampRes::class),
        ApiResponse(code = 401, message = "인증실패", response = BaseResponseBody::class)
    )
    @GetMapping("/stamp")
    fun getStamps(@ApiIgnore authentication: Authentication?): ResponseEntity<out BaseResponseBody>{
        authentication?.let{
            val email = (authentication.details as UserDetails).user.email
            service.findByEmail(email)?.let{
                return ResponseEntity.status(200).body(StampRes.of(200, "성공", it.stamps!!))
            }
        }
        return ResponseEntity.status(401).body(BaseResponseBody.of(401, "인증 실패"))
    }

    @ApiOperation(value="스탬프 추가", notes="사용자의 계정에 스탬프를 추가한다")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = StampRes::class),
        ApiResponse(code = 401, message = "인증실패", response = BaseResponseBody::class),
        ApiResponse(code = 404, message = "스탬프 번호 잘못됨", response = BaseResponseBody::class)
    )
    @PostMapping("/stamp")
    fun addStamp(@ApiIgnore authentication: Authentication?, @ApiParam("스탬프 번호", example = "{ \"stamp\" : 1 }") @RequestBody(required = true) data: Map<String, Int>): ResponseEntity<out BaseResponseBody>{
        authentication?.let{
            val email = (authentication.details as UserDetails).user.email
            if(data["stamp"]!! > 5 || data["stamp"]!! < 1)
                return ResponseEntity.status(404).body(BaseResponseBody.of(404, "스탬프 번호 잘못됨"))
            service.addStamp(email, data["stamp"]!!).also{
                return ResponseEntity.status(200).body(StampRes.of(200, "성공", it))
            }
        }
        return ResponseEntity.status(401).body(BaseResponseBody.of(401, "인증 실패"))
    }
}