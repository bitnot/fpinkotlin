package chapter8.exercises.ex4

import chapter8.RNG
import chapter8.State
import chapter8.nonNegativeInt
import chapter8.solutions.ex4.Gen

data class Gen<A>(val sample: State<RNG, A>) {
    companion object {
        //tag::init[]
        fun choose(start: Int, stopExclusive: Int): Gen<Int> =
            Gen(State { rng: RNG -> nonNegativeInt(rng) }
                .map { it % (stopExclusive - start) + start })
        //end::init[]
    }
}
