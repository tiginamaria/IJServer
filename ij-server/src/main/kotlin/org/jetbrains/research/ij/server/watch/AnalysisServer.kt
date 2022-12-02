package org.jetbrains.research.ij.server.watch

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.nio.file.Path

class AnalysisServer(host: String, port: Int) {

    val serverSocket = aSocket(selectorManager).tcp().bind("127.0.0.1", 9002)

    fun run() {
        runBlocking {
            val selectorManager = SelectorManager(Dispatchers.IO)
            val serverSocket = aSocket(selectorManager).tcp().bind("127.0.0.1", 9002)
            println("Server is listening at ${serverSocket.localAddress}")
            while (true) {
                val socket = serverSocket.accept()
                println("Accepted $socket")
                launch {
                    val receiveChannel = socket.openReadChannel()
                    val sendChannel = socket.openWriteChannel(autoFlush = true)
                    sendChannel.writeStringUtf8("Please enter your name\n")
                    try {
                        while (true) {
                            val name = receiveChannel.readUTF8Line()
                            sendChannel.writeStringUtf8("Hello, $name!\n")
                        }
                    } catch (e: Throwable) {
                        socket.close()
                    }
                }
            }
        }
    }
}
