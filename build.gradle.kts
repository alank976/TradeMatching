import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

group = "io.alank"
version = "1.0-SNAPSHOT"

plugins {
    idea
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.21"
    id("org.springframework.boot") version "2.1.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
    id("com.gorylenko.gradle-git-properties") version "2.0.0"
    kotlin("jvm") version "1.3.21"
    kotlin("kapt") version "1.3.21"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))

    val springBootVersion = "2.1.3.RELEASE"
    val springCloudVersion = "Greenwich.RELEASE"
    implementation(enforcedPlatform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    implementation(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion"))

    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot", "spring-boot-starter-webflux")
    compileOnly("org.springframework.boot", "spring-boot-configuration-processor")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    
    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation("io.projectreactor", "reactor-test")
    testImplementation("io.mockk", "mockk", "1.8.13.kotlin13")
    
    val junit5Version = "5.4.1"
    implementation(enforcedPlatform("org.junit:junit-bom:$junit5Version"))
    testImplementation("org.junit.jupiter", "junit-jupiter-api")
    testImplementation("org.junit.jupiter", "junit-jupiter-params")
    testRuntime("org.junit.jupiter", "junit-jupiter-engine")
//    // for Intellij
    testRuntime("org.junit.platform", "junit-platform-launcher", "1.4.1")
    testRuntime("org.junit.vintage", "junit-vintage-engine")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    withType<BootRun> {
        environment("SPRING_PROFILES_ACTIVE" to "default,local")
    }
}

kapt {
    useBuildCache = true
}

springBoot {
    buildInfo()
}