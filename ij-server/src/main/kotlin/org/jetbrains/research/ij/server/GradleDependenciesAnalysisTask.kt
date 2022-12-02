package org.jetbrains.research.ij.server

import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.util.io.readText
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtPsiFactory
import java.nio.file.Files
import java.nio.file.Path

class GradleDependenciesAnalysisTask(private val filesPath: Path, private val tmpProjectPath: Path) {

    fun run() {
        ApplicationManager.getApplication().invokeAndWait {
            val project = ProjectUtil.openOrImport(tmpProjectPath)
            val factory = KtPsiFactory(project)

            Files.walk(filesPath, 1)
                .filter { Files.isRegularFile(it) && !it.equals(filesPath) }
                .forEach {
                    val fileContent = it.readText()
                    val file = factory.createFile(fileContent)
                    file.accept(
                        object : PsiRecursiveElementVisitor() {
                            override fun visitElement(element: PsiElement) {
                                val callExpression = element as? KtCallExpression
                                callExpression?.let { expr -> println(expr.text) }
                                super.visitElement(element)
                            }
                        }
                    )
                }
        }
    }
}
