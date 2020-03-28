package chapter3.exercises

import chapter3.Cons
import chapter3.List
import chapter3.Nil
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
fun add(xa: List<Int>, xb: List<Int>): List<Int> = when (xa) {
    is Cons -> when (xb) {
        is Cons -> Cons(xa.head + xb.head, add(xa.tail, xb.tail))
        else -> Nil
    }
    else -> Nil
}
// end::init[]

class Exercise_3_21 : WordSpec({
    "list add" should {
        "add elements of two corresponding lists" {
            add(List.of(1, 2, 3), List.of(4, 5, 6)) shouldBe
                List.of(5, 7, 9)
        }
    }
})
