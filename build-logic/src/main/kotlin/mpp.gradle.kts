plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("org.jetbrains.dokka")
}

kotlin {
    explicitApi()
    targets.configureEach {
        compilations.configureEach {
            kotlinOptions {
                allWarningsAsErrors = true
            }
        }
    }
    sourceSets.configureEach {
        languageSettings.progressiveMode = true
    }
    jvmToolchain(11)

    jvm()
}
