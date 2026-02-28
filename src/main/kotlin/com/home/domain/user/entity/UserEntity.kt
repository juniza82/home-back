package com.home.domain.user.entity

import com.home.common.entity.BaseEntity
import com.querydsl.core.annotations.QueryEntity
import jakarta.persistence.*
import java.time.LocalDate


@Entity
@Table(name = "users")
@QueryEntity
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 12)
    var name: String,

    @Column
    var birthday: LocalDate? = null,

    @Column(name = "joindate")
    var joinDate: LocalDate? = null,

    @Column(name = "quitdate")
    var quitDate: LocalDate? = null,

    @Column(length = 12)
    var team: String? = null,

    @Column(length = 12)
    var position: String? = null,

    @Column(name = "positionjob", length = 12)
    var positionJob: String? = null,

    @Column(length = 12)
    var memo: String? = null,

    @Column(nullable = false, length = 50)
    var email: String,

    @Column(length = 100)
    var test: String? = null,

) : BaseEntity() {

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

    fun update(name: String) {
        this.name = name
    }
}
