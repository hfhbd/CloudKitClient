plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka") version "1.6.21"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.9.0"
}

kotlin {
    explicitApi()

    jvm()

    sourceSets {
        commonMain {
            dependencies {
                api(project(":cloudkitclient-core"))
                api("app.softwork:kotlinx-uuid-core:0.0.14")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
