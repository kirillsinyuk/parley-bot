plugins {
	kotlin("jvm") version "2.2.20"
	kotlin("plugin.spring") version "2.2.20"
	kotlin("plugin.jpa") version "2.2.20"
	kotlin("plugin.allopen") version "2.2.20"
	id("org.springframework.boot") version "4.0.5"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.jmailen.kotlinter") version "5.4.2"
	kotlin("kapt") version "2.2.20"
}

group = "com.kvsiniuk"
version = "0.0.1"
description = "parley-bot"

val mapstructVersion = "1.6.3"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.xerial:sqlite-jdbc:3.50.3.0")
	implementation("org.hibernate.orm:hibernate-community-dialects")

	implementation("com.openai:openai-java:4.35.0")

	implementation("org.springframework.retry:spring-retry:2.0.12")
	implementation("org.springframework:spring-aspects")

	implementation("tools.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib")
	implementation("io.github.oshai:kotlin-logging-jvm:7.0.12")

	kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
	implementation("org.mapstruct:mapstruct:$mapstructVersion")

	implementation("com.github.pengrad:java-telegram-bot-api:9.6.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("io.mockk:mockk:1.14.9")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
