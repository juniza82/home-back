package com.home.api.user.dto

import com.home.domain.user.entity.UserEntity

data class UserResDto(
    val id: Long,
    val name: String
) {
    companion object {
        fun from(userEntity: UserEntity): UserResDto {
            return UserResDto(
                id = userEntity.id!!,
                name = userEntity.name
//                age = userEntity.age ?: 0
            )
        }
    }
}