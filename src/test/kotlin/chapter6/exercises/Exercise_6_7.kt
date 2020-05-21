package chapter6.exercises

import chapter3.Cons
import chapter3.List
import chapter3.Nil
import chapter3.solutions.foldRight
import chapter6.RNG
import chapter6.Rand
import chapter6.rng1
import chapter6.unit
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init[]
fun <A> cons(a: A, l: List<A>) = Cons(a, l)
fun <A> sequence(fs: List<Rand<A>>): Rand<List<A>> =
    when (fs) {
        is Nil -> unit(List.empty())
        is Cons -> { rng ->
            val (a, rng2) = fs.head(rng)
            val (xs, rnd3) = sequence(fs.tail)(rng2)
            Cons(a, xs) to rnd3
        }
    }
//end::init[]

//tag::init2[]
fun <A> sequence2(fs: List<Rand<A>>): Rand<List<A>> =
    foldRight(fs, unit(List.empty()), { ra, rla -> map2(ra, rla, ::cons) })
//enc::init2[]

fun ints2(count: Int, rng: RNG): Pair<List<Int>, RNG> {
    fun go(count: Int, acc: List<Rand<Int>>): List<Rand<Int>> =
        if (count <= 0) acc
        else go(count - 1, Cons(RNG::nextInt, acc))
    return sequence(go(count, List.empty()))(rng)
}

class Exercise_6_7 : WordSpec({
    "sequence" should {

        "combine the results of many actions using recursion" {

            val combined: Rand<List<Int>> =
                sequence(
                    List.of(
                        unit(1),
                        unit(2),
                        unit(3),
                        unit(4)
                    )
                )

            combined(rng1).first shouldBe
                List.of(1, 2, 3, 4)
        }

        """combine the results of many actions using
            foldRight and map2""" {

                val combined2: Rand<List<Int>> =
                    sequence2(
                        List.of(
                            unit(1),
                            unit(2),
                            unit(3),
                            unit(4)
                        )
                    )

                combined2(rng1).first shouldBe
                    List.of(1, 2, 3, 4)
            }
    }

    "ints" should {
        "generate a list of ints of a specified length" {
            ints2(4, rng1).first shouldBe
                List.of(1, 1, 1, 1)
        }
    }
})
