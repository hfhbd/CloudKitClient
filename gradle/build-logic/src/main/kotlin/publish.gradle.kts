plugins {
    id("maven-publish")
    id("signing")
    id("io.github.hfhbd.mavencentral")
}

publishing {
    publications.configureEach {
        this as MavenPublication
        pom {
            name.set("app.softwork CloudKit Client Library")
            description.set("A multiplatform Kotlin library to use Apple CloudKit")
            url.set("https://github.com/hfhbd/CloudKitClient")
            licenses {
                license {
                    name.set("Apache-2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("hfhbd")
                    name.set("Philip Wedemann")
                    email.set("mybztg+mavencentral@icloud.com")
                }
            }
            scm {
                connection.set("scm:git://github.com/hfhbd/CloudKitClient.git")
                developerConnection.set("scm:git://github.com/hfhbd/CloudKitClient.git")
                url.set("https://github.com/hfhbd/CloudKitClient")
            }
        }
    }
}

signing {
    val signingKey = providers.gradleProperty("signingKey")
    if (signingKey.isPresent) {
        useInMemoryPgpKeys(signingKey.get(), providers.gradleProperty("signingPassword").get())
        sign(publishing.publications)
    }
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
    filePermissions {}
    dirPermissions {}
}
