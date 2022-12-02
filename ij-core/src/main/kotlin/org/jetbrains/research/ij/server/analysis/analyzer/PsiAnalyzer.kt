package org.jetbrains.research.ij.server.analysis.analyzer

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import java.io.Serializable

abstract class PsiAnalyzer : PsiElementVisitor(), Analyzer {

    abstract fun getResult(): Serializable

    override fun analyze(psiElement: PsiElement): Serializable {
        visitElement(psiElement)

        return getResult()
    }
}
