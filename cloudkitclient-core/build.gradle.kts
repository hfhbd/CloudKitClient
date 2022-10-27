plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka") version "1.7.20"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.12.1"
}

kotlin {
    explicitApi()

    jvm()

    sourceSets {
        // Apache 2, https://github.com/ktorio/ktor/releases/latest
        val ktorVersion = "2.1.2"
        commonMain {
            dependencies {
                api("io.ktor:ktor-client-core:$ktorVersion")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                api("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }
    }
}
