package com.exp.backend.api.guestboard.model.response

import com.exp.backend.api.guestboard.model.entity.GuestNote
import com.exp.backend.common.model.BaseResponseBody

class GuestBoardRes private constructor() : BaseResponseBody(){
    lateinit var guestBoard: List<GuestNote>

    companion object{
        fun of(statusCode: Int, message: String, guestBoard: List<GuestNote>) = GuestBoardRes().apply {
            this.statusCode = statusCode
            this.message = message
            this.guestBoard = guestBoard
        }
    }
}
