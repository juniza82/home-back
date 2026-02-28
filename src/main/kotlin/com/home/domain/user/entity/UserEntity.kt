package com.home.domain.user.entity

import com.home.common.entity.BaseEntity
import com.querydsl.core.annotations.QueryEntity
import jakarta.persistence.*
import java.time.LocalDate


/**
 * 사용자 정보를 관리하는 엔티티 클래스
 * BaseEntity를 상속받아 생성일(createdAt)과 수정일(updatedAt)을 자동으로 관리합니다.
 */
@Entity
@Table(name = "users")
@QueryEntity
class UserEntity(
    /** 고유 식별자 (PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /** 사용자 이름 (필수, 최대 12자) */
    @Column(nullable = false, length = 12)
    var name: String,

    /** 생년월일 */
    @Column
    var birthday: LocalDate? = null,

    /** 입사일 */
    @Column(name = "joindate")
    var joinDate: LocalDate? = null,

    /** 퇴사일 */
    @Column(name = "quitdate")
    var quitDate: LocalDate? = null,

    /** 소속 팀 */
    @Column(length = 12)
    var team: String? = null,

    /** 직책 */
    @Column(length = 12)
    var position: String? = null,

    /** 직무 */
    @Column(name = "positionjob", length = 12)
    var positionJob: String? = null,

    /** 비고/메모 */
    @Column(length = 12)
    var memo: String? = null,

    /** 이메일 (필수, 최대 50자) */
    @Column(nullable = false, length = 50)
    var email: String,

    /** 테스트용 필드 */
    @Column(length = 100)
    var test: String? = null,

) : BaseEntity() {

    /**
     * 회원 생성을 위한 생성자
     */
    constructor(name: String,
                birthday: LocalDate?,
                joinDate: LocalDate?,
                quitDate: LocalDate?,
                team: String?,
                position: String?,
                positionJob: String?,
                memo: String?,
                email: String,
                test: String?) : this(
        id = null,
        name = name,
        birthday = birthday,
        joinDate = joinDate,
        quitDate = quitDate,
        team = team,
        position = position,
        positionJob = positionJob,
        memo = memo,
        email = email,
        test = test
    )

    /**
     * 사용자 정보 수정을 위한 메서드
     */
    fun update(name: String) {
        this.name = name
    }
}
