package com.home.domain.user.service

import com.home.domain.user.entity.UserEntity
import com.home.domain.user.repository.UserQueryRepository
import com.home.domain.user.repository.UserRepository
import com.home.common.exception.BusinessException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService (
    private val userRepository: UserRepository,
    private val userQueryRepository: UserQueryRepository
) {

    @Transactional
    fun saveUser(userEntity: UserEntity): Long {
        return userRepository.save(userEntity).id!!
    }

    fun findUserById(id: Long): UserEntity {
        return userRepository.findById(id)
            .orElseThrow { BusinessException(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다. id: $id") }
    }

    fun findAllUsers(): List<UserEntity> {
        return userRepository.findAll()
    }

    fun findUsersByName(name: String): List<UserEntity> {
        return userQueryRepository.findByName(name)
    }

//    fun findUsersByAgeGreaterThan(age: Int): List<UserEntity> {
//        return userQueryRepository.findByAgeGreaterThan(age)
//    }

    @Transactional
    fun updateUser(id: Long, name: String): UserEntity {
        val user = findUserById(id)
        user.update(name)
        return user
    }

    @Transactional
    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
}