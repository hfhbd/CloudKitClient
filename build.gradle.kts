plugins {
    kotlin("multiplatform") version "1.5.10" apply false
    kotlin("plugin.serialization") version "1.5.10" apply false
}

group = "app.softwork"

repositories {
    mavenCentral()
}

subprojects {
    group = "app.softwork"

    repositories {
        mavenCentral()

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/hfhbd/*")
            credentials {
                username = System.getProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
                password = System.getProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
