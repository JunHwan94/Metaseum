package com.exp.backend

import com.exp.backend.api.user.controller.UserController
import com.exp.backend.api.user.model.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
class UserTest {
    @InjectMocks
    private lateinit var userController: UserController

    @InjectMocks
    private lateinit var objectMapper: ObjectMapper

    @Mock
    private lateinit var userService: UserService

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun init(){
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build()
    }

    @PreAuthorize("")
    fun getMessage(){}

    @Test
    @DisplayName("인증 실패")
    fun postStampNotAuthorizedTest(){
        // given : 어떤 데이터가 있을때
        val data = HashMap<String, Int>().apply{
            this["stamp"] = 5
        }
        //        doReturn(false).when(userService).idExists(email); : 특정한 값을 반환해야 하는 경우
        // when : 어떤 동작을 실행하면
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/stamp")
                .header("Authorization", "잘못된 토큰이다")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data))
        )
        // then : 이런 결과가 나와야 한다
        val mvcResult = resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized).andReturn()
        val status = mvcResult.response.status
        Assertions.assertThat(status).isEqualTo(401)
    }

    @Test
    @DisplayName("스탬프 번호 잘못됨")
    fun postStampNoNotAvailableTest(){
        // given : 어떤 데이터가 있을때
        val data = HashMap<String, Int>().apply{
            this["stamp"] = 6
        }
        //        doReturn(false).when(userService).idExists(email); : 특정한 값을 반환해야 하는 경우
        // when : 어떤 동작을 실행하면
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/stamp")
                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg1ODI4YzU5Mjg0YTY5YjU0YjI3NDgzZTQ4N2MzYmQ0NmNkMmEyYjMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiYXpwIjoiNjM5MjUyNTU2NDc3LXVmcnQzazNqcWQycG80bjNhY2NkODZlMTZiZGJiMDlmLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiNjM5MjUyNTU2NDc3LXVmcnQzazNqcWQycG80bjNhY2NkODZlMTZiZGJiMDlmLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwic3ViIjoiMTA4NTY0MDU4Mjk5MzA5NDQ5ODAxIiwiZW1haWwiOiJ2a2Zma2VobHNzQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoidXlSUENMZXhrTU5IYkhDMktVdGVsUSIsIm5hbWUiOiLsnbzqs6DsnokiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EtL0FPaDE0R2hnNzBuNWE3cFlFUmxBc0hHQlNsNlVXdnFBWndteFNMcVlVRzVNcmZjPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6IuqzoOyeiSIsImZhbWlseV9uYW1lIjoi7J28IiwibG9jYWxlIjoia28iLCJpYXQiOjE2MzYwMDE5NzUsImV4cCI6MTYzNjAwNTU3NSwianRpIjoiMzIzNGMzNjlmNTBkYmZkMTBlYTE1MDc3NmQ0Mzg1Mjk0Njg1Nzc0ZSJ9.TNLypVWzynIMyMMu1ahUuMwkJfnSGH3IkNk6V2MJ_b6B9lszvMiSQjnn3HbhexP-REdB2XuU6loiU-gcbXPPTR_LHSKJGq-pszZPK183St6CUiZ1CV_NyUKAZw_RRO8H_6BHDNRazrQOHmMS6ixygsctpn6LDvrHfsfF51lEj-oNpQg2T8J3lod6OsDanGL1c8EFfPUWJfAVT15XRdkx8azDBSAufNPA8uwSiCXTJx9tj4izmoshiHIdFjiG3nOiOQYNze3RpD1LBqVe31AZpWEBNc7bZW13F2vIMwPbp9dTqrhcyNDkH0PLGCF5SpmZx4YBP6ImvLIKRydyk7uilA")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data))
        )
        // then : 이런 결과가 나와야 한다
        val mvcResult = resultActions.andExpect(MockMvcResultMatchers.status().isNotFound).andReturn()
        val status = mvcResult.response.status
        Assertions.assertThat(status).isEqualTo(404)
    }
}