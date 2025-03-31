plugins {
    id("jvm")
    id("publish")
    id("licensee")
}

dependencies {
    api(libs.ktor.client.java)
    api(libs.serialization.json)
}

publishing {
    publications.register<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.compileJava {
    options.compilerArgumentProviders += object : CommandLineArgumentProvider {

        @get:InputFiles
        @get:PathSensitive(PathSensitivity.RELATIVE)
        val kotlinClasses = tasks.compileKotlin.flatMap { it.destinationDirectory }

        override fun asArguments(): List<String> = listOf(
            "--patch-module",
            "app.softwork.cloudkitclient=${kotlinClasses.get().asFile.absolutePath}"
        )
    }
}
