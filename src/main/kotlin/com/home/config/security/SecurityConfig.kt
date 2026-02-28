package com.home.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Autowired
    @Qualifier("requestFilter")
    private lateinit var requestFilter: OncePerRequestFilter

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.disable() }
//            .authorizeHttpRequests { auth ->
//                auth
//                    .requestMatchers("/h2-console/**").permitAll()
//                    .requestMatchers("/api/**").permitAll()
//                    .anyRequest().authenticated()
//            }
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter::class.java)
            .headers { headers ->
                headers.frameOptions { frameOptions -> frameOptions.disable() }
            }

        return http.build()
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer =
        object: WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedMethods("*")
                    .allowedHeaders("*")
                    .allowedOriginPatterns("*")
                    .exposedHeaders("*")
            }
        }
}

@Configuration
class FilterConfig {
    @Autowired
    private lateinit var environment: Environment

    @Bean
    @Qualifier("requestFilter")
    fun localRequestFilter(
        objectMapper: ObjectMapper
    ): OncePerRequestFilter {
        return RequestFilter(objectMapper)
    }

}
