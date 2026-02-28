package com.home.domain.user.repository

import com.home.domain.user.entity.QUserEntity
import com.home.domain.user.entity.UserEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class UserQueryRepository(
    private val queryFactory: JPAQueryFactory
) {
    
    fun findByName(name: String): List<UserEntity> {
        val user = QUserEntity.userEntity

        return queryFactory
            .selectFrom(user)
            .where(user.name.eq(name))
            .fetch()
    }

//    fun findByAgeGreaterThan(age: Int): List<UserEntity> {
//        val user = QUserEntity.userEntity
//
//        return queryFactory
//            .selectFrom(user)
//            .where(user.age.gt(age))
//            .orderBy(user.age.asc())
//            .fetch()
//    }
}

