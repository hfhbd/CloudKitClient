plugins {
    id("org.jetbrains.dokka")
    id("app.cash.licensee")
}

dokka {
    val module = project.name
    dokkaSourceSets.configureEach {
        reportUndocumented.set(true)
        includes.from("README.md")
        val sourceSetName = name
        File("$module/src/$sourceSetName").takeIf { it.exists() }?.let {
            sourceLink {
                localDirectory.set(file("src/$sourceSetName/kotlin"))
                remoteUrl.set(uri("https://github.com/hfhbd/CloudKitClient/tree/main/$module/src/$sourceSetName/kotlin"))
                remoteLineSuffix.set("#L")
            }
        }
        externalDocumentationLinks {
            register("kotlinx.serialization") {
                url("https://kotlinlang.org/api/kotlinx.serialization/")
            }
        }
    }
}


licensee {
    allow("Apache-2.0")
    allowUrl("https://opensource.org/license/mit")
}
