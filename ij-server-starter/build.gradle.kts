group = rootProject.group
version = rootProject.version

dependencies {
    implementation(rootProject.libs.kotlin.argparser)
    implementation(project(":ij-server"))
}
