plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("org.jetbrains.dokka")
}

kotlin {
    explicitApi()
    compilerOptions {
        allWarningsAsErrors.set(true)
        progressiveMode.set(true)
        optIn.add("kotlin.time.ExperimentalTime")
    }

    jvmToolchain(11)

    jvm()
}
