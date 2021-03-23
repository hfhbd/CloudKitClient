plugins {
    kotlin("multiplatform") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
}

group = "org.hfhbd"
version = "0.0.1"

repositories {
    mavenCentral()
    maven(url = "https://dl.bintray.com/cy6ergn0m/uuid")
    maven(url = "https://kotlin.bintray.com/kotlinx/") // kotlinx.datetime
}

kotlin {
    explicitApi()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    js(BOTH) {
        nodejs()
    }

    
    sourceSets {
        commonMain {
            dependencies {
                api("io.ktor:ktor-client-core:1.5.2")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3-native-mt")
                api("org.jetbrains.kotlinx.experimental:kotlinx-uuid-core:0.0.3")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}
