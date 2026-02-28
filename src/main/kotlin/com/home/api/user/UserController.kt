package com.home.api.user

import com.home.domain.user.entity.UserEntity
import com.home.api.user.dto.UserResDto
import com.home.domain.user.service.UserService
import com.home.common.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/users")
@Tag(name = "회원관리 Api", description = "회원관리 api입니다.")
class UserController (
    private val userService: UserService
) {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    @PostMapping
    @Operation(summary = "회원등록을 위해서 사용하는 API")
    @ApiResponses(value=[
        SwaggerApiResponse(responseCode = "200", description = "Ok", content = [Content(schema = Schema(implementation = ApiResponse::class))]),
        SwaggerApiResponse(responseCode = "400", description = "Bad Request"),
        SwaggerApiResponse(responseCode = "500", description = "Internal Server Error")
    ])
    fun createUser(@Valid @RequestBody request: CreateUserRequest): ApiResponse<CreateUserResponse> {
        log.info("Creating user: ${request.email}")
        val userEntity = UserEntity(
            name = request.name,
            birthday = if (!request.birthday.isNullOrBlank()) LocalDate.parse(request.birthday, dateFormatter) else null,
            joinDate = if (!request.JoinDate.isNullOrBlank()) LocalDate.parse(request.JoinDate, dateFormatter) else null,
            quitDate = if (!request.quitDate.isNullOrBlank()) LocalDate.parse(request.quitDate, dateFormatter) else null,
            team = request.team,
            position = request.position,
            positionJob = request.positionJob,
            memo = request.memo,
            email = request.email,
            test = request.test
        )
        val id = userService.saveUser(userEntity)
        return ApiResponse.success(CreateUserResponse(id), "User created successfully")
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ApiResponse<UserResDto> {
        val user = userService.findUserById(id)
        return ApiResponse.success(UserResDto.from(user))
    }

    @GetMapping
    fun getAllUsers(): ApiResponse<List<UserResDto>> {
        val users = userService.findAllUsers()
        return ApiResponse.success(users.map { UserResDto.from(it) })
    }

    @GetMapping("/search/name")
    fun getUsersByName(@RequestParam name: String): ApiResponse<List<UserResDto>> {
        val users = userService.findUsersByName(name)
        return ApiResponse.success(users.map { UserResDto.from(it) })
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRequest
    ): ApiResponse<UserResDto> {
        val updatedUser = userService.updateUser(id, request.name)
        return ApiResponse.success(UserResDto.from(updatedUser), "User updated successfully")
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "유저 1명 삭제", description = "유저 정보 삭제")
    fun deleteUser(@PathVariable id: Long): ApiResponse<Unit> {
        userService.deleteUser(id)
        return ApiResponse.success(null, "User deleted successfully")
    }


    data class CreateUserRequest(
        @field:NotBlank(message = "이름은 필수입니다.")
        @field:Size(max = 12, message = "이름은 12자 이내여야 합니다.")
        val name: String,

        @field:Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$", message = "생년월일 형식(YYYYMMDD)이 올바르지 않습니다.")
        var birthday: String? = null,

        @field:Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$", message = "입사일 형식(YYYYMMDD)이 올바르지 않습니다.")
        val JoinDate: String? = null,

        @field:Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$", message = "퇴사일 형식(YYYYMMDD)이 올바르지 않습니다.")
        val quitDate: String? = null,

        var team: String? = null,
        var position: String? = null,
        var positionJob: String? = null,
        var memo: String? = null,

        @field:NotBlank(message = "이메일은 필수입니다.")
        @field:Email(message = "이메일 형식이 올바르지 않습니다.")
        var email: String,

        var test: String? = null,
    )

    data class CreateUserResponse(
        val id: Long
    )

    data class UpdateUserRequest(
        @field:NotBlank(message = "이름은 필수입니다.")
        val name: String
    )
}