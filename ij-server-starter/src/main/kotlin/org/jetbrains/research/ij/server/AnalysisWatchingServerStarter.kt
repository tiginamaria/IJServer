package org.jetbrains.research.ij.server

import com.intellij.openapi.application.ApplicationStarter
import com.intellij.openapi.diagnostic.Logger
import com.xenomachina.argparser.ArgParser
import java.nio.file.Paths
import kotlin.system.exitProcess

class AnalysisWatchingServerStarter : ApplicationStarter {

    private val logger = Logger.getInstance(javaClass)

    override fun getCommandName(): String {
        return "watching-server"
    }

    override fun main(args: List<String>) {
        try {
            val parser = ArgParser(args.drop(1).toTypedArray())
            val watchPath by parser.storing(
                "-w",
                "--watchDir",
                help = "Server watching directory"
            ) { Paths.get(this) }

            require(watchPath.toFile().isDirectory) { "Argument --watchDir has to be directory" }

            val watchingServer = AnalysisWatchingServer(watchPath)
            watchingServer.run()

        } catch (ex: Exception) {
            logger.error(ex)
        } finally {
            exitProcess(0)
        }
    }
}
