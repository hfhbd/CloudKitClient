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
                api(libs.uuid)
                api(libs.coroutines.test)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
