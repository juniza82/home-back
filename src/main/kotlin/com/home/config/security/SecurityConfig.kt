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

/**
 * 스프링 시큐리티(Spring Security) 보안 설정 클래스
 * @EnableWebSecurity : 스프링 시큐리티 필터 체인을 활성화
 */
@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Autowired
    @Qualifier("requestFilter")
    private lateinit var requestFilter: OncePerRequestFilter

    /**
     * 보안 필터 체인(SecurityFilterChain) 구성
     * @param http HttpSecurity 빌더 객체
     * @return 빌드된 보안 필터 체인
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // REST API이므로 CSRF 보안 비활성화
            .cors { it.disable() } // CORS는 별도 WebMvcConfigurer에서 설정
            // 현재 개발 단계로 모든 요청 허용 설정 (운영 환경에서는 권한별 설정 필수)
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            // 세션 상태 비저장 방식(Stateless) 설정 (JWT 사용 대비)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            // UsernamePasswordAuthenticationFilter 이전에 커스텀 필터(RequestFilter) 실행
            .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter::class.java)
            // H2 콘솔 등의 프레임 표시를 위해 X-Frame-Options 비활성화
            .headers { headers ->
                headers.frameOptions { frameOptions -> frameOptions.disable() }
            }

        return http.build()
    }

    /**
     * 전역 CORS(Cross-Origin Resource Sharing) 설정
     * 프론트엔드와 백엔드 간 통신을 위해 모든 출처, 모든 헤더, 모든 메서드 허용
     */
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

/**
 * 필터 관련 추가 빈(Bean) 설정 클래스
 */
@Configuration
class FilterConfig {
    @Autowired
    private lateinit var environment: Environment

    /**
     * 커스텀 필터 빈 등록
     * @param objectMapper JSON 변환을 위한 Jackson 라이브러리 객체
     */
    @Bean
    @Qualifier("requestFilter")
    fun localRequestFilter(
        objectMapper: ObjectMapper
    ): OncePerRequestFilter {
        return RequestFilter(objectMapper)
    }

}
