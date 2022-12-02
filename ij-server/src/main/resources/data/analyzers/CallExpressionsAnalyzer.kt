package org.jetbrains.research.ij.server.analysis.example

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.research.ij.server.analysis.RecursivePsiAnalyzer

class CallExpressionsAnalyzer : RecursivePsiAnalyzer() {

    private val callExpressionsNames = mutableListOf<String>()

    override fun getResult(): String {
        return callExpressionsNames.joinToString(separator = System.lineSeparator())
    }

    override fun visitElement(element: PsiElement) {
        val callExpression = element as? KtCallExpression
        callExpression?.let { expr -> callExpressionsNames.add(expr.text) }
        super.visitElement(element)
    }
}
