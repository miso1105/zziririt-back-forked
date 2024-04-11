import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
    kotlin("plugin.noarg") version "1.9.22"
    kotlin("kapt") version "1.9.22"
}

noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

group = "kr.zziririt"
version = "1.1.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val swaggerVersion = "2.2.0"

val caffeineVersion = "3.1.8"

val queryDslVersion = "5.0.0"

val springCloudStarterAwsVersion = "2.0.1.RELEASE"

val awsSesVersion = "1.0.0"

val okhttp3Version = "5.0.0-alpha.11"

val coroutineVersion = "1.6.4"

val kotlinLoggerVersion = "6.0.2"

val dotEnvVersion = "4.0.0"

val jjwtVersion = "0.12.3"

val testH2Version = "2.2.220"

val kotestVersion = "5.5.5"

val kotestExtensionVersion = "1.1.3"

val mockkVersion = "1.13.8"

val redissonVersion = "3.27.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$swaggerVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    //aop
    implementation("org.springframework.boot:spring-boot-starter-aop")
    //cache
    implementation("org.springframework.boot:spring-boot-starter-cache")
    //caffeine
    implementation("com.github.ben-manes.caffeine:caffeine:$caffeineVersion")
    //redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.redisson:redisson:$redissonVersion")
    //querydsl
    implementation("com.querydsl:querydsl-jpa:$queryDslVersion:jakarta")
    kapt("com.querydsl:querydsl-apt:$queryDslVersion:jakarta")
    //aws
    implementation("org.springframework.cloud:spring-cloud-starter-aws:$springCloudStarterAwsVersion")
    //aws ses
    implementation("aws.sdk.kotlin:ses:$awsSesVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttp3Version") // okhttp3 관련 NoSuchMethodError 이슈로 업그레이드
    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutineVersion")
    //log
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggerVersion")
    //.env
    implementation("me.paulschwarz:spring-dotenv:$dotEnvVersion")
    //jwt
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")
    //security
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    //test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.h2database:h2:$testH2Version")
    //kotest
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestExtensionVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    //prometheus
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    //shedLock
    implementation("net.javacrumbs.shedlock:shedlock-spring:5.8.0")
    implementation("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:5.8.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}