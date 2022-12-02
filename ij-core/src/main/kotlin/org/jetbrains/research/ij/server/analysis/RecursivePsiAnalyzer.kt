package org.jetbrains.research.ij.server.analysis

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import java.io.Serializable

abstract class RecursivePsiAnalyzer : PsiRecursiveElementVisitor(), Analyzer {
    abstract fun getResult(): Serializable

    override fun analyze(psiElement: PsiElement): Serializable {
        visitElement(psiElement)

        return getResult()
    }
}
