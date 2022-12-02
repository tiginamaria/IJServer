package org.jetbrains.research.ij.server

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.nio.file.Path

object PathAsStringSerializer : KSerializer<Path> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Path", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Path) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Path = Path.of(decoder.decodeString())
}

@Serializable
enum class InputType {
    FILE, PROJECT
}

@Serializable
data class AnalysisTask(
    val inputType: InputType,
    @Serializable(with = PathAsStringSerializer::class)
    val inputPath: Path,
    @Serializable(with = PathAsStringSerializer::class)
    val outputPath: Path,
    @Serializable(with = PathAsStringSerializer::class)
    val jarPath: Path,
    val analyzerClassName: String,
)
