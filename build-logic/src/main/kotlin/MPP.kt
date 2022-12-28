import org.gradle.api.*
import org.jetbrains.kotlin.gradle.dsl.*

class MPP : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")
        project.plugins.apply("org.jetbrains.kotlin.plugin.serialization")
        project.extensions.configure<KotlinMultiplatformExtension>("kotlin") {
            explicitApi()
            targets.configureEach {
                compilations.configureEach {
                    kotlinOptions {
                        allWarningsAsErrors = true
                    }
                }
            }
            sourceSets.configureEach {
                languageSettings.progressiveMode = true
            }
            jvmToolchain(11)

            jvm()
        }
        project.plugins.apply("org.jetbrains.kotlinx.binary-compatibility-validator")
        project.plugins.apply("org.jetbrains.dokka")
    }
}
