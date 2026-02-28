package com.home.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ModelMapperConfig {

    @Bean
    fun modelMapper() = ModelMapper().apply {
        configuration.setFieldMatchingEnabled(true)
        configuration.setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
        configuration.setFieldMatchingEnabled(true)
        configuration.setAmbiguityIgnored(true)
    }
}