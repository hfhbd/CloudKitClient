plugins {
    id("mpp")
    id("publish")
    id("licensee")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.cloudkitclientCore)
                api("app.softwork:kotlinx-uuid-core:0.0.21")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
