package chapter5.exercises

import chapter3.List
import chapter4.None
import chapter4.Some
import chapter5.Cons
import chapter5.Empty
import chapter5.Stream
import chapter5.solutions.toList
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import chapter3.Cons as ConsL
import chapter3.Nil as NilL
import chapter3.exercises.map as mapL

class Exercise_5_15 : WordSpec({

    //tag::tails[]
    fun <A> Stream<A>.tails(): Stream<Stream<A>> = unfold(this, {
        when (it) {
            is Empty -> None
            is Cons -> Some(Pair(it, it.t()))
        }
    })
    //end::tails[]

    fun <A, B> List<A>.map(f: (A) -> B): List<B> = mapL(this, f)

    "Stream.tails" should {
        "return the stream of suffixes of the input sequence" {
            Stream.of(1, 2, 3)
                .tails()
                .toList()
                .map { it.toList() } shouldBe
                List.of(
                    ConsL(1, ConsL(2, ConsL(3, NilL))),
                    ConsL(2, ConsL(3, NilL)),
                    ConsL(3, NilL)
                )
        }
    }
})
