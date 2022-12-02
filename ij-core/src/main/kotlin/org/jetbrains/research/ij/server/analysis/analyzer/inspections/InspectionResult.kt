package org.jetbrains.research.ij.server.analysis.analyzer.inspections

import kotlinx.serialization.Serializable

@Serializable
data class Problem(
    val name: String,
    val inspector: String,
    val lineNumber: Int,
    val offset: Int?,
    val length: Int?
)

@Serializable
data class InspectionResult(val problems: List<Problem>)
