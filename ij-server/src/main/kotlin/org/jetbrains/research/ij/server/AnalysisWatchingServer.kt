package org.jetbrains.research.ij.server

import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiFileFactory
import com.jetbrains.python.PythonLanguage
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
            val project = ProjectUtil.openOrImport(analysisTask.outputPath)

            val file = PsiFileFactory.getInstance(project)
                .createFileFromText("dummy", PythonLanguage.INSTANCE, "if (a < b and b < c):\n\tprint(a, b, c)")
            val service = InspectionService()
            val inspection = service.inspect(file)
            println(inspection)
        }
        println(analysisTask)
    }

    @OptIn(ObsoleteCoroutinesApi::class, DelicateCoroutinesApi::class)
    fun run() {
        ApplicationManager.getApplication().executeOnPooledThread {
            runBlocking {
                launch {
                    while (true) {
                        watchKey.pollEvents().forEach {
                            acceptEvent(it)
                            delay(1000L)
                        }
                    }
                }
            }
        }
    }
}
