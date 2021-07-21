plugins {
    kotlin("multiplatform") version "1.5.21" apply false
    kotlin("plugin.serialization") version "1.5.21" apply false
}

group = "app.softwork"

repositories {
    mavenCentral()
}

subprojects {
    group = "app.softwork"

    repositories {
        mavenCentral()
    }
}
