pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
    }
}

rootProject.name = "CloudKitClient"

include(":core")
val all: String? by settings
val includeAll = all?.toBoolean() ?: false
if (includeAll) {
    include(":integrationTest")
}
