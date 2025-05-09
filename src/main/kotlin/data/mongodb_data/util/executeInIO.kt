package data.mongodb_data.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

inline fun <T> executeInIO(crossinline block: suspend () -> T): T {
    return runBlocking(Dispatchers.IO) {
        async {
            block()
        }.await()
    }
}