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

object FormatAsStringSerializer : KSerializer<Format> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Format", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Format) = encoder.encodeString(value.value)
    override fun deserialize(decoder: Decoder): Format = Format.valueOf(decoder.decodeString())
}

enum class Format(val value: String) {
    FILE("file"), PROJECT("project")
}

@Serializable
data class AnalysisTask(
    @Serializable(with = PathAsStringSerializer::class)
    val projectsPath: Path,
    @Serializable(with = PathAsStringSerializer::class)
    val outputPath: Path,
    @Serializable(with = FormatAsStringSerializer::class)
    val format: Format
)
