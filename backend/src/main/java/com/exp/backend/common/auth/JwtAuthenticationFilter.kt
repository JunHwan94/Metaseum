package com.exp.backend.common.auth

import com.exp.backend.api.user.model.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class JwtAuthenticationFilter
constructor(authenticationManager: AuthenticationManager, private val userService: UserService): BasicAuthenticationFilter(authenticationManager) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        // 헤더 읽기
        val header = request.getHeader(JwtUtil.HEADER_STRING)
        log.debug("헤더 읽음")

        // 토큰에 접두어 없으면 안됨
        if(header == null || !header.startsWith(JwtUtil.TOKEN_PREFIX)){
            log.debug("헤더값 유효하지 않음")
            chain.doFilter(request, response)
            return
        }

        try{
            val authentication = getAuthentication(request)
            log.debug(authentication.toString())
            // 헤더 있으면 사용자 정보를 불러와서 인증 처리
            SecurityContextHolder.getContext().authentication = authentication
        }catch (e: Exception){
            log.error("catch : 인증 실패")
        }
        chain.doFilter(request, response)
    }

    @Transactional(readOnly = true)
    @Throws(java.lang.Exception::class)
    open fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = request.getHeader(JwtUtil.HEADER_STRING)
        // 헤더의 Authorization 키에 토큰 값이 있을 때 인증처리
        if (token != null) {
            log.debug("토큰 : $token")
            val verifier = JwtUtil.getVerifier()
//            JwtUtil.handleError(token)
            val googleIdToken = verifier.verify(token.replace(JwtUtil.TOKEN_PREFIX, ""))
            val userEmail = googleIdToken.payload.email

            if (userEmail != null) {
                log.debug("이메일 : $userEmail")
                // 토큰에 포함된 이메일로 사용자 조회
                val user = userService.findByEmail(userEmail)
                log.debug("사용자 이름 : ${user?.name}")
                if (user != null) {
                    log.debug("user exists : ${user.name}")
                    // 존재하는 사용자면 인증 정보 생성
                    val userDetails = UserDetails(user)
                    val jwtAuthentication = UsernamePasswordAuthenticationToken(
                        userEmail,
                        null, userDetails.authorities
                    )
                    jwtAuthentication.details = userDetails
                    return jwtAuthentication
                }
                log.debug(user)
            }
            return null
        }
        return null
    }
}