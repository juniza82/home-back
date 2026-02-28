import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
	kotlin("kapt") version "1.9.25"
}

group = "com.home"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4")

	// QueryDSL 의존성 추가
	implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
	kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
	kapt("jakarta.annotation:jakarta.annotation-api")
	kapt("jakarta.persistence:jakarta.persistence-api")

	// JDSL
	implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter-jakarta:2.2.1.RELEASE")

	// 로깅 관련 의존성
	implementation("org.slf4j:slf4j-api")
	implementation("ch.qos.logback:logback-classic")

	// Flyway
	implementation("org.flywaydb:flyway-core:11.5.0")
	implementation("org.flywaydb:flyway-database-postgresql:11.5.0")

	// DB
	runtimeOnly("org.postgresql:postgresql")

	// swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")

	// p6Spy
	implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.10.0")

	// ObjectMapper
	implementation("org.modelmapper:modelmapper:3.2.2")

	// HTML Parser
	implementation("org.jsoup:jsoup:1.18.1")

	// jwt
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:3.0.4")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

// Querydsl 설정부
val querydslDir = "src/main/generated"

sourceSets {
	main {
		kotlin.srcDir(querydslDir)
	}
}

tasks.withType<JavaCompile> {
	options.generatedSourceOutputDirectory.set(file(querydslDir))
}

// clean 태스크 실행 시 생성된 Q클래스 파일 삭제
tasks.named("clean") {
	doLast {
		file(querydslDir).deleteRecursively()
	}
}

kapt {
	arguments {
		arg("querydsl.entityAccessors", "true")
	}
	generateStubs = true
}

tasks.withType<Test> {
	useJUnitPlatform()
}
