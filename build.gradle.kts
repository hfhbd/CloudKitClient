plugins {
    kotlin("multiplatform") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.21"
}

group = "org.hfhbd"
version = "0.0.1"

repositories {
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
        val coroutinesVersion: String by project
        commonMain {
            dependencies {
                val ktorVersion: String by project
                api("io.ktor:ktor-client-core:$ktorVersion")
                val jsonVersion: String by project
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:$jsonVersion")
                val dateTimeVersion: String by project
                api("org.jetbrains.kotlinx:kotlinx-datetime:$dateTimeVersion")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                val uuidVersion: String by project
                api("org.jetbrains.kotlinx.experimental:kotlinx-uuid-core:$uuidVersion")
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
