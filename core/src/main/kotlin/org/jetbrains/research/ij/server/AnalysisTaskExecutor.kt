package org.jetbrains.research.ij.server

import com.intellij.util.io.delete
import java.nio.file.Path
import java.nio.file.Paths

class AnalysisTaskExecutor {

    fun analyze(projectPath: Path, projectTmpFolderPath: Path): Boolean {
        val tempFolderPath = Paths.get(analysisTask.outputPath.toString(), "tmp")
        tempFolderPath.delete(recursively = true)

    }

    fun execute(analysisTask: AnalysisTask) {
        val tempFolderPath = Paths.get(analysisTask.outputPath.toString(), "tmp")
        tempFolderPath.delete(recursively = true)

        var projectIndex = 0
        getSubdirectories(analysisTask.projectsPath).forEach { projectPath ->
            println("Start analyzing project ${projectPath.fileName} (index=${++projectIndex})")
            val isSuccessful = analyze(projectPath, tempFolderPath.resolve(projectPath.fileName))
            println("Project ${projectPath.fileName} (index=$projectIndex) " +
                    "was analyzed successfully: $isSuccessful")
        }

        tempFolderPath.delete()
    }
}
