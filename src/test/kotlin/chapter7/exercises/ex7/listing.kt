package chapter7.exercises.ex7

import chapter7.exercises.ex7.Pars.fork
import chapter7.exercises.ex7.Pars.unit
import java.lang.System.exit
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

typealias Par<A> = (ExecutorService) -> Future<A>

object Pars {

    fun <A> unit(a: A): Par<A> =
        { es: ExecutorService -> UnitFuture(a) }

    data class UnitFuture<A>(val a: A) : Future<A> {

        override fun get(): A = a

        override fun get(timeout: Long, timeUnit: TimeUnit): A = a

        override fun cancel(evenIfRunnning: Boolean): Boolean = false

        override fun isDone(): Boolean = true

        override fun isCancelled(): Boolean = false
    }

    fun <A> fork(
        a: () -> Par<A>
    ): Par<A> =
        { es: ExecutorService ->
            es.submit(Callable<A> { a()(es).get() })
        }

    fun <A> lazyUnit(a: () -> A): Par<A> = fork { unit(a()) }

    fun <A> run(a: Par<A>): A = TODO()
}

/*
fun breaking(): Int = 3

fun main() {
    // WORKIE
    // val es: ExecutorService = Executors.newWorkStealingPool()
    // val es: ExecutorService = Executors.newFixedThreadPool(2)
    // val es: ExecutorService = Executors.newCachedThreadPool()
    val es: ExecutorService = ForkJoinPool()

    // NO WORKIE
    // val es: ExecutorService = Executors.newSingleThreadExecutor()
    // val es: ExecutorService = Executors.newFixedThreadPool(1)

    es.submit() {
    }

    exit(0)
}

 */

fun <A> wrap(value: A, times: Int): Par<A> =
    if (times <= 0) {
        unit(value)
    } else {
        fork { wrap(value, times - 1) }
    }

fun <A> eq(par1: Par<A>, par2: Par<A>, es: ExecutorService) =
    par1(es).get(1, TimeUnit.SECONDS) == par2(es).get(1, TimeUnit.SECONDS)

fun main() {
    val threads = 10
    val es: ExecutorService = Executors.newFixedThreadPool(threads)

    println("running")
    val x = 1
    assert(
        eq(unit(x), wrap(x, threads + 1), es)
    )
    println("done")
    exit(0)
}