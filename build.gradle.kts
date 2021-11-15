plugins {
    kotlin("multiplatform") version "1.6.0" apply false
    kotlin("plugin.serialization") version "1.6.0" apply false
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = "app.softwork"

repositories {
    mavenCentral()
}

nexusPublishing {
    repositories {
        sonatype {
            username.set(System.getProperty("sonartype.apiKey") ?: System.getenv("SONARTYPE_APIKEY"))
            password.set(System.getProperty("sonartype.apiToken") ?: System.getenv("SONARTYPE_APITOKEN"))
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

allprojects {
    apply(plugin = "org.gradle.maven-publish")
    apply(plugin = "org.gradle.signing")

    group = "app.softwork"

    repositories {
        mavenCentral()
    }

    group = "app.softwork"

    task("emptyJar", Jar::class) {
    }

    publishing {
        publications.all {
            if (this is MavenPublication) {
                artifact(tasks.getByName("emptyJar")) {
                    classifier = "javadoc"
                }
                pom {
                    name.set("app.softwork CloudKit Client Library")
                    description.set("A multiplatform Kotlin library to use Apple CloudKit")
                    url.set("https://github.com/hfhbd/CloudKitClient")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
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
    }
    (System.getProperty("signing.privateKey") ?: System.getenv("SIGNING_PRIVATE_KEY"))?.let {
        String(java.util.Base64.getDecoder().decode(it)).trim()
    }?.let { key ->
        println("found key, config signing")
        signing {
            val signingPassword = System.getProperty("signing.password") ?: System.getenv("SIGNING_PASSWORD")
            useInMemoryPgpKeys(key, signingPassword)
            sign(publishing.publications)
        }
    }
}
