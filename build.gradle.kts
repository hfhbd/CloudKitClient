plugins {
    id("org.jetbrains.dokka")
}

dependencies {
    for (sub in subprojects) {
        dokka(sub)
    }
}

dokka {
    dokkaPublications.configureEach {
        includes.from("README.md")
    }
}
