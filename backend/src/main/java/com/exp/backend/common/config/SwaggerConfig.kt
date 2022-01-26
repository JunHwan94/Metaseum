package com.exp.backend.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.UiConfiguration
import springfox.documentation.swagger.web.UiConfigurationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
open class SwaggerConfig {

    @Bean
    open fun api(): Docket =
        Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
//            .paths(PathSelectors.ant("/htg/**"))
//            .paths(PathSelectors.ant("/users/myname"))
            .build()
            .securityContexts(listOf(securityContext()))
            .securitySchemes(listOf(apiKey()))

    open fun apiKey() = ApiKey(SECURITY_SCHEMA_NAME, "Authorization", "header")

    open fun securityContext(): SecurityContext =
        SecurityContext.builder()
            .securityReferences(defaultAuth())
            .build()

    val SECURITY_SCHEMA_NAME = "JWT"
    val AUTHORIZATION_SCOPE_GLOBAL = "global"
    val AUTHORIZATION_SCOPE_GLOBAL_DESC = "accessEverything"

    open fun defaultAuth(): List<SecurityReference>? {
        val authorizationScope = AuthorizationScope(AUTHORIZATION_SCOPE_GLOBAL, AUTHORIZATION_SCOPE_GLOBAL_DESC)
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return arrayListOf(SecurityReference(SECURITY_SCHEMA_NAME, authorizationScopes))
    }

    @Bean
    open fun uiConfig(): UiConfiguration = UiConfigurationBuilder.builder().build()
}