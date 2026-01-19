plugins {
    id("maven-publish")
    id("signing")
    id("io.github.hfhbd.mavencentral")
}

publishing {
    publications.withType<MavenPublication>().configureEach {
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
    useInMemoryPgpKeys(
        providers.gradleProperty("signingKey").orNull,
        providers.gradleProperty("signingPassword").orNull,
    )
    isRequired = providers.gradleProperty("signingKey").isPresent
    sign(publishing.publications)
}
