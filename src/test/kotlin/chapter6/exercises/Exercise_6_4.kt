package chapter6.exercises

import chapter3.Cons
import chapter3.List
import chapter6.RNG
import chapter6.rng1
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init[]
fun ints(count: Int, rng: RNG): Pair<List<Int>, RNG> =
    generateSequence(rng.nextInt()) { (_, r) -> r.nextInt() }
        .take(count)
        .fold(List.empty<Int>() to rng,
            { (xs, _), (i, r) -> Cons(i, xs) to r })
//end::init[]

/**
 * TODO: Re-enable tests by removing `!` prefix!
 */
class Exercise_6_4 : WordSpec({

    "ints" should {
        "generate a list of ints of a specified length" {

            ints(5, rng1) shouldBe
                Pair(List.of(1, 1, 1, 1, 1), rng1)
        }
    }
})
