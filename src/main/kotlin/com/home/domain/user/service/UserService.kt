package com.home.domain.user.service

import com.home.domain.user.entity.UserEntity
import com.home.domain.user.repository.UserQueryRepository
import com.home.domain.user.repository.UserRepository
import com.home.common.exception.BusinessException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 회원 관련 비즈니스 로직을 처리하는 서비스 클래스
 * @property userRepository 기본 JPA 레포지토리
 * @property userQueryRepository QueryDSL 기반 커스텀 레포지토리
 */
@Service
@Transactional(readOnly = true)
class UserService (
    private val userRepository: UserRepository,
    private val userQueryRepository: UserQueryRepository
) {

    /**
     * 회원 정보 저장
     * @param userEntity 저장할 회원 정보
     * @return 생성된 회원의 ID
     */
    @Transactional
    fun saveUser(userEntity: UserEntity): Long {
        return userRepository.save(userEntity).id!!
    }

    /**
     * ID를 통한 회원 조회
     * @param id 조회할 회원 ID
     * @return 조회된 회원 엔티티
     * @throws BusinessException 회원을 찾을 수 없는 경우 NOT_FOUND 상태 반환
     */
    fun findUserById(id: Long): UserEntity {
        return userRepository.findById(id)
            .orElseThrow { BusinessException(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다. id: $id") }
    }

    /**
     * 전체 회원 목록 조회
     * @return 회원 엔티티 목록
     */
    fun findAllUsers(): List<UserEntity> {
        return userRepository.findAll()
    }

    /**
     * 이름을 통한 회원 목록 검색
     * @param name 검색할 이름
     * @return 일치하는 회원 엔티티 목록 (QueryDSL 활용)
     */
    fun findUsersByName(name: String): List<UserEntity> {
        return userQueryRepository.findByName(name)
    }

//    fun findUsersByAgeGreaterThan(age: Int): List<UserEntity> {
//        return userQueryRepository.findByAgeGreaterThan(age)
//    }

    /**
     * 회원 이름 수정
     * @param id 수정할 회원 ID
     * @param name 새로운 이름
     * @return 수정된 회원 엔티티
     */
    @Transactional
    fun updateUser(id: Long, name: String): UserEntity {
        val user = findUserById(id) // 존재하지 않을 경우 BusinessException 발생
        user.update(name) // 변경 감지(Dirty Check)에 의해 트랜잭션 종료 시 반영됨
        return user
    }

    /**
     * 회원 탈퇴/삭제
     * @param id 삭제할 회원 ID
     */
    @Transactional
    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
}