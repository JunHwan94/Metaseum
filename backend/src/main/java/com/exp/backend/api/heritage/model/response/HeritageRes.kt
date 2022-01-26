package com.exp.backend.api.heritage.model.response;

import com.exp.backend.api.heritage.model.entity.Heritage
import com.exp.backend.common.model.BaseResponseBody
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("문화재 조회 응답")
class HeritageRes private constructor() : BaseResponseBody() {
    @ApiModelProperty(name = "문화재 정보")
    lateinit var heritage: Heritage

    companion object {
        fun of(statusCode: Int, message: String, heritage: Heritage) =
            HeritageRes().apply {
                this.statusCode = statusCode
                this.message = message
                this.heritage = heritage
            }
    }
}
