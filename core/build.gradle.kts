plugins {
    id("jvm")
    id("publish")
    id("licensee")
}

dependencies {
    api(libs.ktor.client.core)
    api(libs.serialization.json)

    api(libs.ktor.client.java)
}

publishing {
    publications.register<MavenPublication>("maven") {
        from(components["java"])
    }
}
