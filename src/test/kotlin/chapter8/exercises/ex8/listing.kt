package chapter8.exercises.ex8

import chapter8.RNG
import chapter8.State
import chapter8.double

data class Gen<A>(val sample: State<RNG, A>) {
    companion object {

        fun double(): Gen<Double> =
            Gen(State { rng -> double(rng) })

        //tag::init[]
        fun <A> weighted(
            pga: Pair<Gen<A>, Double>,
            pgb: Pair<Gen<A>, Double>
        ): Gen<A> =
            double().flatMap { d ->
                val (ga, pa) = pga
                val (gb, pb) = pgb
                if (d * (pa + pb) <= pa) ga
                else gb
            }
        //end::init[]
    }

    fun <B> flatMap(f: (A) -> Gen<B>): Gen<B> =
        Gen(sample.flatMap { a -> f(a).sample })
}
