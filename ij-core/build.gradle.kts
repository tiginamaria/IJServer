group = rootProject.group
version = rootProject.version

apply {
    plugin(libs.plugins.kotlin.serialization.get().pluginId)
}

dependencies {
    implementation(rootProject.libs.kotlinx.serialization.json)
}
