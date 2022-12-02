package org.jetbrains.research.ij.server.watch

import com.intellij.ide.impl.ProjectUtil
import com.intellij.util.io.readText
import com.intellij.util.io.write
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.research.ij.server.analysis.Analyzer
import org.jetbrains.research.ij.server.analysis.RecursivePsiAnalyzer
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path

class AnalysisTaskProcessor {
    private fun <T : Analyzer> loadAnalyzerFromJar(className: String, jarPath: Path): T {
        val url = jarPath.toFile().toURI().toURL()
        val classLoader = URLClassLoader(arrayOf(url), Analyzer::class.java.classLoader)
        val clazz = classLoader.loadClass(className)
        return clazz.getDeclaredConstructor().newInstance() as? T ?: error("Can not load class")
    }

    private fun analyzeFiles(filesPath: Path, outputPath: Path, jarPath: Path, analyzerClassName: String) {
        val analyzer = loadAnalyzerFromJar<RecursivePsiAnalyzer>(analyzerClassName, jarPath)
        val project = ProjectUtil.openOrImport(Path.of("."))
        val factory = KtPsiFactory(project)

        Files.walk(filesPath, 1)
            .filter { Files.isRegularFile(it) }
            .forEach {
                val fileContent = it.readText()
                val file = factory.createFile(fileContent)
                val result = analyzer.analyze(file)
                outputPath.write(result.toString())
            }
    }

    fun accept(task: AnalysisTask) {
        analyzeFiles(task.inputPath, task.outputPath, task.jarPath, task.analyzerClassName)
    }
}
