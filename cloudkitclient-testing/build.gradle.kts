plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka") version "1.7.20"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.11.1"
}

kotlin {
    explicitApi()

    jvm()

    sourceSets {
        commonMain {
            dependencies {
                api(project(":cloudkitclient-core"))
                api("app.softwork:kotlinx-uuid-core:0.0.17")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
