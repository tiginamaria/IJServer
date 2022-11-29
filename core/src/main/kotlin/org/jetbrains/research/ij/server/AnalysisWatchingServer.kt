package org.jetbrains.research.ij.server

import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds.*

class AnalysisWatchingServer(private val projectsInfoPath: Path) {

    private val watchService = FileSystems.getDefault().newWatchService()
    private val watchKey = projectsInfoPath.register(watchService, ENTRY_CREATE)
    private val analysisTaskExecutor = AnalysisTaskExecutor()

    @OptIn(ExperimentalSerializationApi::class)
    fun run() = runBlocking {
        launch(Dispatchers.IO) {
            while (true) {
                watchKey.pollEvents().forEach {
                    val eventPath = projectsInfoPath.resolve(it.context() as Path)
                    val eventType = it.kind()

                    println("$eventType: $eventPath")
                    val analysisTask = Json.decodeFromStream<AnalysisTask>(Files.newInputStream(eventPath))
                    println(analysisTask)
                    analysisTaskExecutor.execute(analysisTask)
                }
                delay(1000L)
            }
        }
    }
}

fun main() {
    val server = AnalysisWatchingServer(
        Path.of("/Users/tiginamaria1999/IdeaProjects/IJServer/core/src/main/resources/data")
    )

    server.run()
}
