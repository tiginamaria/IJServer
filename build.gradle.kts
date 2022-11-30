group = "org.jetbrains.research.ij.server"
version = "1.0-SNAPSHOT"

@Suppress("DSL_SCOPE_VIOLATION") // "libs" produces a false-positive warning, see https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
    alias(libs.plugins.gradle.ktlint)
    alias(libs.plugins.jetbrains.intellij)
    alias(libs.plugins.kotlin.serialization)
}

val platformVersion: String by project
val platformType: String by project
val platformDownloadSources: String by project
val platformPlugins: String by project
val pluginName: String by project

val gradleVersion: String by project

allprojects {
    apply {
        plugin(rootProject.libs.plugins.kotlin.jvm.get().pluginId)
        plugin(rootProject.libs.plugins.dokka.get().pluginId)
        plugin(rootProject.libs.plugins.gradle.ktlint.get().pluginId)
        plugin(rootProject.libs.plugins.jetbrains.intellij.get().pluginId)
    }

    repositories {
        mavenCentral()
    }

    intellij {
        version.set(platformVersion)
        type.set(platformType)
        downloadSources.set(platformDownloadSources.toBoolean())
        updateSinceUntilBuild.set(true)
        plugins.set(platformPlugins.split(',').map(String::trim).filter(String::isNotEmpty))
    }

    ktlint {
        disabledRules.set(setOf("no-wildcard-imports"))
        enableExperimentalRules.set(true)
        filter {
            exclude("**/resources/**")
        }
    }

    tasks {
        runIde {

            val watchDir: String? by project

            args = listOfNotNull(
                "watching-server",
                watchDir?.let { "--watchDir=$it" }
            )

            jvmArgs = listOf(
                "-Djava.awt.headless=true",
                "--add-exports",
                "java.base/jdk.internal.vm=ALL-UNNAMED",
                "-Djdk.module.illegalAccess.silent=true"
            )

            maxHeapSize = "32g"

            standardInput = System.`in`
            standardOutput = System.`out`
        }

        withType<org.jetbrains.intellij.tasks.BuildSearchableOptionsTask>()
            .forEach { it.enabled = false }
    }
}
