plugins {
    kotlin("multiplatform") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.21"
}

group = "org.hfhbd"
version = "0.0.1"

repositories {
    mavenCentral()
    jcenter()
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
                api("io.ktor:ktor-client-core:1.5.0")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2-native-mt")
                api("org.jetbrains.kotlinx.experimental:kotlinx-uuid-core:0.0.2")
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
