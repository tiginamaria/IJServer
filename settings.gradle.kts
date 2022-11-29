
rootProject.name = "IJServer"
include("core")

enableFeaturePreview("VERSION_CATALOGS")

val utilitiesRepo = "https://github.com/JetBrains-Research/plugin-utilities.git"
val utilitiesProjectName = "org.jetbrains.research.pluginUtilities"

sourceControl {
    gitRepository(java.net.URI.create(utilitiesRepo)) {
        producesModule("$utilitiesProjectName:plugin-utilities-core")
        producesModule("$utilitiesProjectName:plugin-utilities-python")
        producesModule("$utilitiesProjectName:plugin-utilities-test")
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://nexus.gluonhq.com/nexus/content/repositories/releases")
    }
}
