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
                api("app.softwork:kotlinx-uuid-core:0.0.17")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
