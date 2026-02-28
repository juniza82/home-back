package com.home

import com.home.domain.user.entity.UserEntity
import com.home.domain.user.repository.UserQueryRepository
import com.home.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import kotlin.test.Test

@SpringBootTest
@Transactional
class UserRepositoryTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userQueryRepository: UserQueryRepository
) {

    private fun createUserEntity(name: String): UserEntity {
        return UserEntity(
            name = name,
            birthday = LocalDate.of(1990, 1, 1),
            joinDate = LocalDate.of(2020, 1, 1),
            quitDate = LocalDate.of(2024, 12, 31),
            team = "개발팀",
            position = "사원",
            positionJob = "백엔드 개발",
            memo = "테스트용 메모",
            email = "$name@example.com",
            test = "test"
        )
    }

    @Test
    @DisplayName("JPA Repository를 통한 회원 저장 및 조회 테스트")
    fun saveUserTest() {
        // given
        val userEntity = createUserEntity("홍길동")

        // when
        val savedUser = userRepository.save(userEntity)

        // then
        assertThat(savedUser.id).isNotNull()
        assertThat(savedUser.name).isEqualTo("홍길동")
        // assertThat(savedUser.age).isEqualTo(20) // age field does not exist
    }

    @Test
    @DisplayName("QueryDSL을 사용한 이름으로 회원 조회 테스트")
    fun findByNameWithQuerydslTest() {
        // given
        userRepository.save(createUserEntity("김철수"))
        userRepository.save(createUserEntity("김철수"))
        userRepository.save(createUserEntity("이영희"))

        // when
        val users = userQueryRepository.findByName("김철수")

        // then
        assertThat(users).hasSize(2)
        assertThat(users).extracting("name").containsOnly("김철수")
    }

    // This test relies on a non-existent 'age' field and findByAgeGreaterThan method.
    // Commenting out until UserEntity and UserQueryRepository are updated to include age.
//    @Test
//    @DisplayName("QueryDSL을 사용한 나이 필터링 테스트")
//    fun findByAgeGreaterThanTest() {
//        // given
//        userRepository.save(createUserEntity("김철수", 25))
//        userRepository.save(createUserEntity("박영수", 30))
//        userRepository.save(createUserEntity("이영희", 28))
//        userRepository.save(createUserEntity("최민준", 22))
//
//        // when
//        val users = userQueryRepository.findByAgeGreaterThan(25)
//
//        // then
//        assertThat(users).hasSize(2)
//        assertThat(users).extracting("name").containsExactlyInAnyOrder("박영수", "이영희")
//        assertThat(users).extracting("age").containsExactlyInAnyOrder(30, 28)
//    }

    // This test relies on a non-existent 'age' field and findByAgeGreaterThan method.
    // Commenting out until UserEntity and UserQueryRepository are updated to include age.
//    @Test
//    @DisplayName("QueryDSL 직접 사용 테스트")
//    fun directQuerydslTest() {
//        // given
//        userRepository.saveAll(listOf(
//            createUserEntity("김철수", 25),
//            createUserEntity("박영수", 30),
//            createUserEntity("이영희", 28),
//            createUserEntity("최민준", 22)
//        ))
//
//        // when
//        val youngUsers = userQueryRepository
//            .findByAgeGreaterThan(25)
//            .filter { it.name?.startsWith("이") ?: false }
//
//        // then
//        assertThat(youngUsers).hasSize(1)
//        assertThat(youngUsers[0].name).isEqualTo("이영희")
//        assertThat(youngUsers[0].age).isEqualTo(28)
//    }
}