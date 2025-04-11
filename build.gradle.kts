plugins {
    kotlin("jvm") version "2.0.21"
}

allprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }

    group = "com.example"
    version = "0.0.1"
}

subprojects {

}

kotlin {
    jvmToolchain(21)
}