package com.home.config.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile(value = ["local"])
@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenApi(@Value("\${springdoc.swagger-ui.version}") appVersion: String): OpenAPI {
        return OpenAPI()
            .info(
                Info().also { info ->
                    info.title = "Home API"
                    info.version = appVersion
                    info.description = "Home Swagger API Doc"
                    info.termsOfService = "https://swagger.io/terms/"
                    info.license =
                        License().also { license ->
                            license.name = "Apache 2.0"
                            license.url = "https://springdoc.org"
                        }
                }
            )
//            .addSecurityItem(SecurityRequirement().addList("JWT-Token"))
//            .component(
//                Component().addSecuritySchemes(
//                    "JWT-Token",
//                    SecurityScheme().also {
//                        it.type = SecurityScheme.Type.HTTP
//                        it.scheme = "bearer"
//                        it.bearerFormat = "JWT"
//                    }
//                )
//            )
    }

    // swagger 에서 페이지를 나눌 때 사용하면 된다.
    // 앞에 path를 사용하여 특정 API 들만 불러오게끔 사용.
    @Bean
    fun appApi(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("App API")
            .pathsToExclude("/admin/**")
            .build()
}