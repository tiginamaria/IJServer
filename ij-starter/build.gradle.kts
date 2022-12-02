group = rootProject.group
version = rootProject.version

dependencies {
    implementation(rootProject.libs.kotlin.argparser)
    implementation(project(":ij-server"))
}

tasks {
    runIde {

        val watchDir: String? by project

        args = listOfNotNull(
            "analysis-server",
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
}
