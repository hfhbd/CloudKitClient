plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka") version "1.4.32"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.5.0"
    `maven-publish`
}

kotlin {
    explicitApi()

    jvm()

    sourceSets {
        // Apache 2, https://github.com/ktorio/ktor/releases/latest
        val ktorVersion = "1.5.4"
        commonMain {
            dependencies {
                api("io.ktor:ktor-client-core:$ktorVersion")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.2.0")
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

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/hfhbd/cloudkitclient")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
