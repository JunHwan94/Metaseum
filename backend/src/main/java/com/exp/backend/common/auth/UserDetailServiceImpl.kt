package com.exp.backend.common.auth

import com.exp.backend.api.user.model.service.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailServiceImpl constructor(private val userService: UserService): UserDetailsService {
    override fun loadUserByUsername(email: String?): UserDetails? {
        email?.let{
            userService.findByEmail(email)?.let{
                return UserDetails(it)
            }
        }
        return null
    }
}