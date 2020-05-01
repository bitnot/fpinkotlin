package chapter5.exercises

import chapter3.List
import chapter3.exercises.reverse
import chapter5.Cons
import chapter5.Empty
import chapter5.Stream
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import chapter3.Cons as LCons

class Exercise_5_1 : WordSpec({
    //tag::init[]
    fun <A> Stream<A>.toList(): List<A> {
        tailrec fun <A> go(s: Stream<A>, l: List<A>): List<A> =
            when (s) {
                is Empty -> l
                is Cons -> go(s.t(), LCons(s.h(), l))
            }

        return reverse(go(this, List.empty()))
    }
    //end::init[]

    "Stream.toList" should {
        "force the stream into an evaluated list" {
            val s = Stream.of(1, 2, 3, 4, 5)
            s.toList() shouldBe List.of(1, 2, 3, 4, 5)
        }
    }
})
