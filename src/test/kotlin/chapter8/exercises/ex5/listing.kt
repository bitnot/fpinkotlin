package chapter8.exercises.ex5

import chapter8.RNG
import chapter8.State

data class Gen<A>(val sample: State<RNG, A>) {
    companion object {

        //tag::init[]
        fun <A> unit(a: A): Gen<A> = Gen(State { rng -> a to rng })

        fun boolean(): Gen<Boolean> = Gen(State<RNG, Boolean> {
            val (i, rng1) = it.nextInt()
            (i % 2 == 0) to rng1
        })

        fun <A> listOfN(n: Int, ga: Gen<A>): Gen<List<A>> =
            Gen(State.sequence(List(n) { ga.sample }))

        fun <A> listOfNDummy(n: Int, ga: Gen<A>): Gen<List<A>> =
            Gen(State { rng ->
                generateSequence(n to ga.sample.run(rng)) { kar ->
                    val (k, ar) = kar
                    val (a, r) = ar
                    if (k <= 0) null
                    else (k - 1) to ga.sample.run(r)
                }.fold(emptyList<A>() to rng) { acc, pair ->
                    val (xs, _) = acc
                    val (_, aToRng) = pair
                    val (a, lastRng) = aToRng
                    xs + listOf(a) to lastRng
                }
            })
        //end::init[]
    }
}
