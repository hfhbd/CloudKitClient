plugins {
    id("org.jetbrains.dokka")
    id("io.gitlab.arturbosch.detekt")
}

dokka {
    dokkaPublications.configureEach {
        includes.from("README.md")
    }

    dependencies {
        for (sub in subprojects) {
            dokka(sub)
        }
    }
}

detekt {
    source.from(fileTree(layout.settingsDirectory) {
        include("**/*.kt")
        exclude("**/*.kts")
        exclude("**/resources/**")
        exclude("**/generated/**")
        exclude("**/build/**")
        exclude("**/test/**")
    })
    parallel = true
    autoCorrect = true
    buildUponDefaultConfig = true
    reports {
        sarif.required.set(true)
    }

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${detekt.toolVersion}")
    }
}
