package com.exp.backend.api.guestboard.model.response

import com.exp.backend.api.guestboard.model.entity.GuestNote
import com.exp.backend.common.model.BaseResponseBody

class GuestNoteNoRes private constructor() : BaseResponseBody(){
    var guestNoteNo = 1

    companion object{
        fun of(statusCode: Int, message: String, guestNoteNo: Int) = GuestNoteNoRes().apply {
            this.statusCode = statusCode
            this.message = message
            this.guestNoteNo = guestNoteNo
        }
    }
}
