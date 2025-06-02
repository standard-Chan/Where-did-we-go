plugins {
    id("java")
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.4"
}

apply(plugin = "io.spring.dependency-management")

group = "com.wheredidwego"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2") // ✅ JUnit 5 명확하게 지정
    testRuntimeOnly("org.junit.platform:junit-platform-launcher") // ✅ 런처 추가

    //h2 console
    runtimeOnly ("com.h2database:h2")

    // lombok
    compileOnly ("org.projectlombok:lombok:1.18.36")
    annotationProcessor ("org.projectlombok:lombok:1.18.36")
    // lombok test
    testCompileOnly ("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor ("org.projectlombok:lombok:1.18.36")

    // spring boot
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // spring  boot 유효성 검사
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // H2 Database (테스트용 인메모리 DB)
    //runtimeOnly("com.h2database:h2")

    // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // OAuth2
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    //tymeleaf
    implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")

    // AWS S3 v2
    implementation("software.amazon.awssdk:s3:2.29.52")

    //MYSQL
    implementation("mysql:mysql-connector-java:8.0.33")

    // org
    implementation("org.json:json:20240303")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

    // QueryDSL
    implementation ("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor ("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor ("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor ("jakarta.persistence:jakarta.persistence-api")
}

tasks.test {
    useJUnitPlatform()
}