package org.jetbrains.research.ij.server.analysis

import com.intellij.psi.PsiElement
import java.io.Serializable

interface Analyzer {
    fun analyze(psiElement: PsiElement): Serializable
}
