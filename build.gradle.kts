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
    implementation("org.springframework.boot:spring-boot-starter-test")

    // H2 Database (테스트용 인메모리 DB)
    runtimeOnly("com.h2database:h2")

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
}

tasks.test {
    useJUnitPlatform()
}