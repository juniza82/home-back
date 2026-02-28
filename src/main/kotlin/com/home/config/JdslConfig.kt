package com.home.config

import com.linecorp.kotlinjdsl.query.creator.CriteriaQueryCreatorImpl
import com.linecorp.kotlinjdsl.query.creator.SubqueryCreatorImpl
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactoryImpl
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class JdslConfig(
    @PersistenceContext private val em: EntityManager
) {
    @Bean
    fun queryJDSLFactory(): SpringDataQueryFactory {
        return SpringDataQueryFactoryImpl(
            criteriaQueryCreator = CriteriaQueryCreatorImpl(em),
            subqueryCreator = SubqueryCreatorImpl()
        )
    }
}