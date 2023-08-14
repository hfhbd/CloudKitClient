plugins {
    id("mpp")
    id("publish")
    id("licensee")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.ktor.client.core)
                api(libs.serialization.json)
                api(libs.datetime)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.coroutines.test)
            }
        }
        named("jvmMain") {
            dependencies {
                api(libs.ktor.client.cio)
            }
        }
    }
}
