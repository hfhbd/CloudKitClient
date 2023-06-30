plugins {
    id("MPP")
    id("publish")
    id("licensee")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.cloudkitclientCore)
                api("app.softwork:kotlinx-uuid-core:0.0.20")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.2")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
