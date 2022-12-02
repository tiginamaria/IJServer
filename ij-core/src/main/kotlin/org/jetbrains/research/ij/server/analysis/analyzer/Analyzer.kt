package org.jetbrains.research.ij.server.analysis.analyzer

import com.intellij.psi.PsiElement

interface Analyzer {
    fun analyze(psiElement: PsiElement): Any?
}