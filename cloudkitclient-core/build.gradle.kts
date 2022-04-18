plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka") version "1.6.20"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.9.0"
}

kotlin {
    explicitApi()

    jvm()

    sourceSets {
        // Apache 2, https://github.com/ktorio/ktor/releases/latest
        val ktorVersion = "2.0.0"
        commonMain {
            dependencies {
                api("io.ktor:ktor-client-core:$ktorVersion")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
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
