package com.exp.backend.common.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("기본 응답")
open class BaseResponseBody protected constructor() {
    @ApiModelProperty(name = "응답 메세지", example = "정상")
    lateinit var message: String
    @ApiModelProperty(name = "응답 코드", example = "200")
    var statusCode: Int? = null

    companion object {
        fun of(statusCode: Int, message: String) =
            BaseResponseBody().apply {
                this.message = message
                this.statusCode = statusCode
            }
    }
}
