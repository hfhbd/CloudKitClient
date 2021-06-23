plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm {
        testRuns["test"].executionTask.configure {
            environment = System.getProperties() as Map<String, *>
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":cloudkitclient-core"))
                api("app.softwork:kotlinx-uuid-core:0.0.6")
            }
        }
        commonTest {
            dependencies {
                implementation(project(":cloudkitclient-testing"))
                implementation(kotlin("test"))
            }
        }
    }
}
