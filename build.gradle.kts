val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.0.21"
    id("io.ktor.plugin") version "3.0.1"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.insert-koin:koin-ktor:3.5.6")
    implementation("io.github.oshai:kotlin-logging:5.1.0")
    implementation("io.ktor:ktor-serialization-jackson-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.0")

    implementation("org.seleniumhq.selenium:selenium-java:4.26.0")
    implementation("io.github.bonigarcia:webdrivermanager:5.9.2")
    implementation("com.cronutils:cron-utils:9.2.1")
    implementation("com.github.lolmageap.ktor-server-extension:scheduler:1.0.2")
    implementation("io.ktor:ktor-server-status-pages")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
