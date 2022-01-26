package com.exp.backend.api.user.model.response

import com.exp.backend.common.model.BaseResponseBody

class StampRes private constructor() : BaseResponseBody(){
    lateinit var stamps: HashMap<String, Int>

    companion object{
        fun of(statusCode: Int, message: String, stamps: HashMap<String, Int>) =
            StampRes().apply {
                this.statusCode = statusCode
                this.message = message
                this.stamps = stamps
            }
    }
}