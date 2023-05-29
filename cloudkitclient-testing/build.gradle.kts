plugins {
    mpp
    publish
    licensee
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.cloudkitclientCore)
                api("app.softwork:kotlinx-uuid-core:0.0.20")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
