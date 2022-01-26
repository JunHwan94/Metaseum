package com.exp.backend.common.auth

import com.exp.backend.api.user.model.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetails constructor(val user: User) : UserDetails{
    var accountNonExpired = false
    var accountNonLocked = false
    var credentialNonExpired = false
    var enabled = false
    private var roles = ArrayList<GrantedAuthority>()

    fun setAuthorities(roles: ArrayList<GrantedAuthority>){
        this.roles = roles
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = roles

    override fun getPassword(): String = ""

    override fun getUsername(): String = user.email

    override fun isAccountNonExpired(): Boolean = accountNonExpired

    override fun isAccountNonLocked(): Boolean = accountNonLocked

    override fun isCredentialsNonExpired(): Boolean = credentialNonExpired

    override fun isEnabled(): Boolean = enabled
}