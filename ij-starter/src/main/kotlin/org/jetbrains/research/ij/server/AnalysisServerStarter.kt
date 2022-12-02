package org.jetbrains.research.ij.server

import com.intellij.openapi.application.ApplicationStarter
import com.intellij.openapi.diagnostic.Logger
import com.xenomachina.argparser.ArgParser
import org.jetbrains.research.ij.server.watch.AnalysisWatchServer
import java.nio.file.Paths
import kotlin.system.exitProcess

class AnalysisServerStarter : ApplicationStarter {

    private val logger = Logger.getInstance(javaClass)

    override fun getCommandName(): String {
        return "analysis-server"
    }

    override fun main(args: List<String>) {
        try {
            val parser = ArgParser(args.drop(1).toTypedArray())
            val watchPath by parser.storing(
                "-w",
                "--watchDir",
                help = "Server watching directory to receive tasks"
            ) { Paths.get(this) }

            require(watchPath.toFile().isDirectory) { "Argument --watchDir has to be directory" }

            val watchingServer = AnalysisWatchServer(watchPath)
            watchingServer.run()
        } catch (ex: Exception) {
            logger.error(ex)
        } finally {
            exitProcess(0)
        }
    }
}
