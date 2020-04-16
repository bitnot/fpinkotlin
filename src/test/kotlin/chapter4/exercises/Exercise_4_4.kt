package chapter4.exercises

import chapter3.Cons
import chapter3.List
import chapter4.Boilerplate.foldRight
import chapter4.None
import chapter4.Option
import chapter4.Some
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init[]
fun <A> sequence(xs: List<Option<A>>): Option<List<A>> = xs.foldRight(
    Option.of(List.empty()),
    { headOption, tailOption ->
        headOption.flatMap { head ->
            tailOption.map { tail ->
                Cons(head, tail)
            }
        }
    }
)
//end::init[]

class Exercise_4_4 : WordSpec({

    "sequence" should {
        "turn a list of some options into an option of list" {
            val lo =
                List.of(Some(10), Some(20), Some(30))
            sequence(lo) shouldBe Some(List.of(10, 20, 30))
        }
        "turn a list of options containing none into a none" {
            val lo =
                List.of(Some(10), None, Some(30))
            sequence(lo) shouldBe None
        }
    }
})
