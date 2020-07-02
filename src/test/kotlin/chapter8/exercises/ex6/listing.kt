package chapter8.exercises.ex6

import chapter8.RNG
import chapter8.State

//tag::init[]
data class Gen<A>(val sample: State<RNG, A>) {

    companion object {
        fun <A> listOfN(gn: Gen<Int>, ga: Gen<A>): Gen<List<A>> =
            Gen(gn.sample.flatMap { n -> State.sequence(List(n) { ga.sample }) })
    }

    fun <B> flatMap(f: (A) -> Gen<B>): Gen<B> =
        Gen(sample.flatMap { a -> f(a).sample })

    fun <B> flatMapDummy(f: (A) -> Gen<B>): Gen<B> = Gen(State { rng ->
        val (a, rng1) = sample.run(rng)
        f(a).sample.run(rng1)
    })
}
//end::init[]
