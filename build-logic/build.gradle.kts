plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.plugins.kotlin.mpp.toDep())
    implementation(libs.plugins.kotlin.serialization.toDep())
    implementation(libs.plugins.licensee.toDep())
    implementation(libs.plugins.binary.toDep())
    implementation(libs.plugins.dokka.toDep())
    implementation(libs.plugins.publish.toDep())
}

gradlePlugin {
    plugins {
        register("MPP") {
            id = "mpp"
            implementationClass = "MPP"
        }
    }
}

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}
