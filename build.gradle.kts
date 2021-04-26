plugins {
    kotlin("multiplatform") version "1.5.0" apply false
    kotlin("plugin.serialization") version "1.5.0" apply false
    id("org.jetbrains.dokka") version "1.4.32"
}

group = "app.softwork"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
        maven(url = "https://dl.bintray.com/cy6ergn0m/uuid")
    }
}
