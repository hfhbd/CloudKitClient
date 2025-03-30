plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("org.jetbrains.dokka")
    id("java-test-fixtures")
    id("jvm-test-suite")
}

kotlin {
    explicitApi()
    compilerOptions {
        allWarningsAsErrors.set(true)
        progressiveMode.set(true)
        optIn.add("kotlin.time.ExperimentalTime")
    }

    jvmToolchain(11)
}

java {
    withJavadocJar()
    withSourcesJar()
}

testing.suites.withType(JvmTestSuite::class).configureEach {
    useKotlinTest()
}
