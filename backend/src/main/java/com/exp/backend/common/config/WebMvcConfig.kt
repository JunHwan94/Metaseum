package com.exp.backend.common.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CommonsRequestLoggingFilter
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver
import javax.servlet.Filter

@Configuration
open class WebMvcConfig : WebMvcConfigurer{
    @Bean
    open fun corsConfigurationSource() : CorsConfigurationSource{
        val configuration = CorsConfiguration().apply {
            addAllowedOriginPattern("*")
            addAllowedMethod("*")
            addAllowedHeader("*")
            allowCredentials = true
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.apply{
            addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources")
            addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/")
            addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
            addResourceHandler("/images/**").addResourceLocations("file:///tmp/tomcat.8080.2703000389849287224/work/Tomcat/localhost/ROOT/images/")

            addResourceHandler("/css/**").addResourceLocations("classpath:/dist/css/")
            addResourceHandler("/fonts/**").addResourceLocations("classpath:/dist/fonts/")
            addResourceHandler("/icons/**").addResourceLocations("classpath:/dist/icons/")
            addResourceHandler("/img/**").addResourceLocations("classpath:/dist/img/")
            addResourceHandler("/js/**").addResourceLocations("classpath:/dist/js/")
            addResourceHandler("/models/**").addResourceLocations("classpath:/dist/models/")
            addResourceHandler("/**").addResourceLocations("classpath:/dist/")
                .resourceChain(true)
                .addResolver(object : PathResourceResolver(){
                    override fun getResource(resourcePath: String, location: Resource): Resource? {
                        val requestResource = location.createRelative(resourcePath)
                        return if(requestResource.exists() && requestResource.isReadable) requestResource
                        else ClassPathResource("/dist/index.html")
                    }
                })
        }
    }

    @Bean
    open fun loggingFilterRegistration(): FilterRegistrationBean<Filter>{
        return FilterRegistrationBean(requestLoggingFilter()).apply { urlPatterns.add("/api/*") }
    }

    open fun requestLoggingFilter(): Filter{
        return CommonsRequestLoggingFilter().apply{
            setIncludeClientInfo(true)
            setIncludeQueryString(true)
            setIncludePayload(true)
            setIncludeHeaders(true)
            setMaxPayloadLength(64000)
        }
    }
}