package chapter7.exercises.ex2

import io.kotlintest.specs.WordSpec

sealed class Par<T> {
    companion object {
        fun <A> unit(a: A): Par<A> = ParUnit(a)

        fun <A, B, C> map2(a: Par<A>, b: Par<B>, f: (A, B) -> C): Par<C> =
            when (a) {
                is ParUnit -> when (b) {
                    is ParUnit -> ParUnit(f(a.value, b.value))
                    is ParFork -> TODO()
                }
                is ParFork -> when (b) {
                    is ParUnit -> TODO()
                    is ParFork -> TODO()
                }
            }

        fun <A> fork(a: () -> Par<A>): Par<A> = ParFork { run(a()) }

        fun <A> lazyUnit(a: () -> A): Par<A> = fork { ParUnit(a()) }

        fun <A> run(a: Par<A>): A = when (a) {
            is ParUnit -> a.value
            is ParFork -> a.produce()
        }
    }
}

class ParUnit<T>(val value: T) : Par<T>()

class ParFork<T>(val produce: () -> T) : Par<T>() //TODO: consider suspend

class Solution_7_2 : WordSpec({

    "Par" should {
        "create a computation that immediately results in a value" {
            Par.unit { 1 }
        }
        """combine the results of two parallel computations with 
            a binary function""" {
            Par.map2(
                Par.unit(1),
                Par.unit(2)
            ) { i: Int, j: Int -> i + j }
        }
        "mark a computation for concurrent evaluation by run" {
            Par.fork { Par.unit { 1 } }
        }
        "wrap expression a for concurrent evaluation by run" {
            Par.lazyUnit { 1 }
        }
        """fully evaluate a given Par spawning computations 
            and extracting value""" {
            Par.run(Par.unit { 1 })
        }
    }
})
