plugins {
    kotlin("multiplatform") version "1.4.31" apply false
    kotlin("plugin.serialization") version "1.4.31" apply false
    id("org.jetbrains.dokka") version "1.4.20"
}

group = "app.softwork"
version = "0.0.1"

repositories {
    mavenCentral()
    jcenter() // dokka
}

subprojects {
    repositories {
        mavenCentral()
        maven(url = "https://dl.bintray.com/cy6ergn0m/uuid")
        maven(url = "https://kotlin.bintray.com/kotlinx/") // kotlinx.datetime
    }
}