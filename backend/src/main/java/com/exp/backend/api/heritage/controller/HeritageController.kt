package com.exp.backend.api.heritage.controller;

import com.exp.backend.api.heritage.model.response.HeritageRes
import com.exp.backend.api.heritage.model.response.HeritagesRes
import com.exp.backend.api.heritage.model.service.HeritageService
import com.exp.backend.common.model.BaseResponseBody
import io.swagger.annotations.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["문화재 API"])
@RestController
@RequestMapping("/htg")
class HeritageController constructor(private val service: HeritageService) {

    @ApiOperation(value="문화재 리스트 조회", notes="문화재 리스트 전체 조회")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = HeritagesRes::class)
    )
    @GetMapping("/list")
    fun getList(): ResponseEntity<out BaseResponseBody>{
        return ResponseEntity.status(200).body(HeritagesRes.of(200, "성공", service.findAll()))
    }

    @ApiOperation(value="문화재 조회", notes="문화재 코드로 조회")
    @ApiResponses(
        ApiResponse(code = 200, message = "성공", response = HeritageRes::class),
        ApiResponse(code = 404, message = "실패", response = BaseResponseBody::class)
    )
    @GetMapping("/{code}")
    fun getHeritage(@ApiParam(value = "문화재 코드") @PathVariable code: String): ResponseEntity<out BaseResponseBody>{
        service.findByCode(code)?.let{
            return ResponseEntity.status(200).body(HeritageRes.of(200, "성공", it))
        }
        return ResponseEntity.status(404).body(BaseResponseBody.of(404, "존재하지 않는 code"))
    }
}