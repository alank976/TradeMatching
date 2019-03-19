import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

group = "io.alank"
version = "1.0-SNAPSHOT"
val junit5Version = "5.3.2"

plugins {
    idea
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.11"
    id("org.springframework.boot") version "2.1.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    id("com.gorylenko.gradle-git-properties") version "2.0.0"
    kotlin("jvm") version "1.3.11"
    kotlin("kapt") version "1.3.11"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencyManagement {
    val springCloudVersion = "Finchley.RELEASE"
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        mavenBom("org.junit:junit-bom:$junit5Version")
    }
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))

    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot", "spring-boot-starter-webflux")
    compileOnly("org.springframework.boot", "spring-boot-configuration-processor")
    kapt("org.springframework.boot:spring-boot-configuration-processor")


    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation("io.projectreactor", "reactor-test")
    testImplementation("io.mockk", "mockk", "1.8.13.kotlin13")
    testImplementation("org.junit.jupiter", "junit-jupiter-api")
    testImplementation("org.junit.jupiter", "junit-jupiter-params")
    testRuntime("org.junit.jupiter", "junit-jupiter-engine")
//    // for Intellij
    testRuntime("org.junit.platform", "junit-platform-launcher", "1.3.2")
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