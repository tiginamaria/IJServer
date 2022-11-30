package org.jetbrains.research.ij.server

import com.intellij.openapi.application.ApplicationManager
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds.*
import java.nio.file.WatchEvent

class AnalysisWatchingServer(private val watchPath: Path) {

    private val watchService = FileSystems.getDefault().newWatchService()
    private val watchKey = watchPath.register(watchService, ENTRY_CREATE)

    @OptIn(ExperimentalSerializationApi::class)
    private fun acceptEvent(event: WatchEvent<*>) {
        val eventType = event.kind()
        val eventPath = watchPath.resolve(event.context() as Path)

        println("Accept event type=$eventType path=$eventPath")

        val analysisTask = Json.decodeFromStream<AnalysisTask>(Files.newInputStream(eventPath))

        ApplicationManager.getApplication().invokeAndWait {
            println("I'm in application")
        }
        println(analysisTask)
    }

    fun run() {
        val scope = ApplicationManager.getApplication().getService(MyService::class.java)

        runBlocking {
            scope.scheduleSomethingUseful().join()
        }
    }
}
