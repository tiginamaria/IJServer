package org.jetbrains.research.ij.server

import com.intellij.openapi.components.Service
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager

@Service(Service.Level.APP)
class MyService : Disposable {

    private val cs = CoroutineScope(EmptyCoroutineContext)

    override fun dispose() {
        cs.cancel()
    }

    fun scheduleSomethingUseful(): Job {
        return cs.launch {
            while (true) {
                ApplicationManager.getApplication().invokeAndWait {
                    println("I'm in application")
                }
            }
        }
    }
}
