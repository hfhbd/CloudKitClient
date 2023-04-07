plugins {
    `kotlin-dsl`
}

dependencies {
    val kotlin = "1.8.20"
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlin")
    implementation("app.cash.licensee:licensee-gradle-plugin:1.6.0")
    implementation("org.jetbrains.kotlinx.binary-compatibility-validator:org.jetbrains.kotlinx.binary-compatibility-validator.gradle.plugin:0.13.0")
    implementation("org.jetbrains.dokka:org.jetbrains.dokka.gradle.plugin:1.8.10")
}

gradlePlugin {
    plugins {
        register("MyRepos") {
            id = "MyRepos"
            implementationClass = "MyRepos"
        }
        register("MPP") {
            id = "mpp"
            implementationClass = "MPP"
        }
    }
}
