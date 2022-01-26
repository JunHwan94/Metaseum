package com.exp.backend.api.user.model.response

import com.exp.backend.api.user.model.entity.User
import com.exp.backend.common.model.BaseResponseBody
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("사용자 정보 응답")
class UserRes private constructor() : BaseResponseBody() {
    @ApiModelProperty(name = "사용자 정보")
    lateinit var user: User
    @ApiModelProperty(name = "토큰")
    var token: String? = null

    companion object{
        fun of(statusCode: Int, message: String, user: User, token: String? = "") =
            UserRes().apply {
                this.statusCode = statusCode
                this.message = message
                this.user = user
                this.token = token
            }
    }
}