package com.exp.backend.api.guestboard.controller

import com.exp.backend.api.guestboard.model.response.GuestBoardRes
import com.exp.backend.api.guestboard.model.response.GuestNoteNoRes
import com.exp.backend.api.guestboard.model.service.GuestBoardService
import com.exp.backend.common.model.BaseResponseBody
import io.swagger.annotations.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(tags = ["방명록 API"])
@RestController
@RequestMapping("/guestboard")
class GuestBoardController constructor(private val service: GuestBoardService) {

    @ApiOperation(value="방명록 리스트 조회", notes="방명록 리스트 전체 조회")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = GuestBoardRes::class)
    )
    @GetMapping("list")
    fun getList(): ResponseEntity<out BaseResponseBody>{
        return ResponseEntity.status(200).body(GuestBoardRes.of(200, "성공", service.findAll()))
    }

    @ApiOperation(value="방명록 등록", notes="방명록 등록")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = BaseResponseBody::class),
        ApiResponse(code = 500, message = "서버 에러", response = BaseResponseBody::class)
    )
    @PostMapping
    fun postGuestNote(@ApiParam("등록할 방명록 이미지 url", example = "{\"guestNoteUrl\":\"https://~~~~\"}") @RequestBody data: Map<String, String>): ResponseEntity<out BaseResponseBody>{
        val guestNoteNo = service.upsertByNo(data["guestNoteUrl"]!!)
        return if(guestNoteNo != -1) // 예외처리?
            ResponseEntity.status(200).body(GuestNoteNoRes.of(200, "성공", guestNoteNo))
        else ResponseEntity.status(500).body(BaseResponseBody.of(500, "서버 에러"))
    }
}