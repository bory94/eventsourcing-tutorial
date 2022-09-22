package com.bory.eventsourcingtutorial.core.infrastructure.extensions

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import kotlin.math.pow

private val LOGGER: Logger =
    LoggerFactory.getLogger("com.bory.eventsourcingtutorial.infrastructure.extensions.Retry")

fun <R> retry(
    times: Int = 5,
    sleepingDuration: Duration = Duration.ofSeconds(2),
    exponentialSleep: Boolean = false,
    skipRetryExceptions: Array<Class<out Throwable>> = emptyArray(),
    block: () -> R
): R {
    fun tryBlock(block: () -> R): Either<R> =
        try {
            Either(block(), null)
        } catch (throwable: Throwable) {
            Either(null, throwable)
        }

    fun sleepingTime(i: Int) = (
            if (exponentialSleep) sleepingDuration.toMillis().toDouble().pow(i - 1) else 1.0
            ).toLong()

    var either: Either<R>? = null
    for (i in 1..times) {
        either = tryBlock(block)
        if (either.throwable == null) return either.result!!

        if (skipRetryExceptions.any { it.isAssignableFrom(either.throwable!!.javaClass) })
            throw either.throwable!!

        LOGGER.debug("Retrying #${i}: $block throws ${either.throwable!!.javaClass.canonicalName} ::: ${either.throwable!!.message}")
        if (i != times) {
            try {
                Thread.sleep(sleepingTime(i))
            } catch (e: InterruptedException) {/*ignored*/
            }
        }
    }

    throw either!!.throwable!!
}

data class Either<R>(val result: R?, val throwable: Throwable?)