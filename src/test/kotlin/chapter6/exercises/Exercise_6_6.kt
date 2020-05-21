package chapter6.exercises

import chapter6.Rand
import chapter6.rng1
import chapter6.unit
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init[]
fun <A, B, C> map2(ra: Rand<A>, rb: Rand<B>, f: (A, B) -> C): Rand<C> =
    { rng ->
        val (a, rng2) = ra(rng)
        val (b, rng3) = rb(rng2)
        f(a, b) to rng3
    }
//end::init[]

/**
 * TODO: Re-enable tests by removing `!` prefix!
 */
class Exercise_6_6 : WordSpec({
    "map2" should {

        "combine the results of two actions" {

            val combined: Rand<String> =
                map2(
                    unit(1.0),
                    unit(1), { d, i ->
                        ">>> $d double; $i int"
                    })

            combined(rng1).first shouldBe ">>> 1.0 double; 1 int"
        }
    }
})
