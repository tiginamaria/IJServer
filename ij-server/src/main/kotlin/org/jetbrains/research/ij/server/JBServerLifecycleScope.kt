package org.jetbrains.research.ij.server

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.util.concurrency.AppExecutorUtil
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import java.lang.Integer.max

@Service(Service.Level.APP)
class JBServerLifecycleScope : CoroutineScope, Disposable {

    private inline val threadCount
        get() = max(1, 2 * Runtime.getRuntime().availableProcessors() / 3)

    internal val coroutineDispatcher =
        AppExecutorUtil.createBoundedApplicationPoolExecutor(
            /* name = */ this::class.simpleName!!,
            /* maxThreads = */ threadCount
        ).asCoroutineDispatcher()

    private val supervisor = SupervisorJob()

    override val coroutineContext = supervisor + CoroutineName(this::class.qualifiedName!!) + coroutineDispatcher

    override fun dispose() {
        supervisor.invokeOnCompletion { coroutineDispatcher.close() }
        println("Disposing ${this::class.simpleName}")
    }
}
