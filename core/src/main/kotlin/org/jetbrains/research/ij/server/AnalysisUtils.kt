package org.jetbrains.research.ij.server

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

fun getSubdirectories(path: Path): Stream<Path> {
    return Files.walk(path, 1)
        .filter { Files.isDirectory(it) && !it.equals(path) }
}