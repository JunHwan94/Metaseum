package com.exp.backend.common.config

import com.exp.backend.api.user.model.service.UserService
import com.exp.backend.common.auth.JwtAuthenticationFilter
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class SecurityConfig constructor(private val userService: UserService, private val userDetailService: UserDetailsService) : WebSecurityConfigurerAdapter() {
    override fun configure(web: WebSecurity){
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider())
    }

    @Bean
    open fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    open fun authenticationProvider(): DaoAuthenticationProvider? {
        val daoAuthenticationProvider = DaoAuthenticationProvider()
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder())
        daoAuthenticationProvider.setUserDetailsService(userDetailService)
        return daoAuthenticationProvider
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/user/myname", "/user/info", "/user/stamp", "/user/character").authenticated()
            .anyRequest().permitAll()
            .and()
            .formLogin().loginPage("/")
            .permitAll()
            .and()
            .logout().logoutSuccessUrl("/")
            .permitAll()
            .and()
            .addFilter(JwtAuthenticationFilter(authenticationManager(), userService))
    }
}