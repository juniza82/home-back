package com.home.common.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

/**
 * 모든 엔티티의 공통 필드(생성일, 수정일)를 관리하는 추상 클래스
 * @MappedSuperclass : 테이블로 매핑되지 않고 자식 엔티티에게 매핑 정보만 제공
 * @EntityListeners(AuditingEntityListener::class) : JPA Auditing 기능을 통해 시간을 자동 기록
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {

    /** 데이터 생성 시점 자동 기록 */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null
        protected set

    /** 데이터 수정 시점 자동 기록 */
    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null
        protected set
}
