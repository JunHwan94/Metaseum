package com.exp.backend.api.heritage.model.response;

import com.exp.backend.api.heritage.model.entity.Heritage
import com.exp.backend.common.model.BaseResponseBody
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("문화재 리스트 응답")
class HeritagesRes private constructor() : BaseResponseBody() {
    @ApiModelProperty("문화재 리스트")
    lateinit var heritages: List<Heritage>

    companion object {
        fun of(statusCode: Int, message: String, heritages: List<Heritage>) =
            HeritagesRes().apply {
                this.statusCode = statusCode
                this.message = message
                this.heritages = heritages
            }
    }
}
