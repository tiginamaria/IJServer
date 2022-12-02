package org.jetbrains.research.ij.server.watch

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import java.nio.file.WatchEvent

class AnalysisWatchServer(private val watchPath: Path) {

    private val watchService = FileSystems.getDefault().newWatchService()
    private val watchKey = watchPath.register(watchService, ENTRY_CREATE)
    private val analysisTaskProcessor = AnalysisTaskProcessor()

    @OptIn(ExperimentalSerializationApi::class)
    private fun acceptEvent(event: WatchEvent<*>) {
        val eventType = event.kind()
        val eventPath = watchPath.resolve(event.context() as Path)

        println("Accept event type=$eventType path=$eventPath")

        val analysisTask = Json.decodeFromStream<AnalysisTask>(Files.newInputStream(eventPath))
        println("Accept event task=$analysisTask")

        analysisTaskProcessor.accept(analysisTask)
    }

    fun run() {
        runBlocking {
            launch {
                while (true) {
                    watchKey.pollEvents().forEach {
                        acceptEvent(it)
                    }
                    delay(1000L)
                }
            }.join()
        }
    }
}
