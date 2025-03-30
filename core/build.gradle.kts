plugins {
    id("mpp")
    id("publish")
    id("licensee")
    id("java-test-fixtures")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.ktor.client.core)
                api(libs.serialization.json)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        named("jvmMain") {
            dependencies {
                api(libs.ktor.client.java)
            }
        }
    }
}
