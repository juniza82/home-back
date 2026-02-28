package com.home.domain.user.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.home.domain.user.entity.UserEntity
import org.springframework.stereotype.Repository


@Repository
class UserJdslRepository(
    private val queryJDSLFactory: SpringDataQueryFactory,
) {
    fun findByName(name: String): List<UserEntity> =
        queryJDSLFactory.listQuery<UserEntity> {
            select(entity(UserEntity::class))
            from(entity(UserEntity::class))
            where(col(UserEntity::name).equal(name))
        }

    fun findByAgeGreaterThan(age: Int): List<UserEntity> =
        queryJDSLFactory.listQuery<UserEntity> {
            select(entity(UserEntity::class))
            from(entity(UserEntity::class))
//            where(col(UserEntity::age).greaterThan(age))
        }
}