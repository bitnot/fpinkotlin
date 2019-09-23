package chapter5.solutions

import chapter3.List
import chapter3.solutions.reverse
import chapter5.Cons
import chapter5.Empty
import chapter5.Stream
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import chapter3.Cons as ConsL
import chapter3.Nil as NilL

//tag::init[]
//Unsafe! Naive solution could cause a stack overflow.
fun <A> Stream<A>.toListUnsf(): List<A> = when (this) {
    is Empty -> NilL
    is Cons -> ConsL(this.h(), this.t().toListUnsf())
}

fun <A> Stream<A>.toList(): List<A> {
    tailrec fun go(xs: Stream<A>, acc: List<A>): List<A> = when (xs) {
        is Empty -> acc
        is Cons -> go(xs.t(), ConsL(xs.h(), acc))
    }
    return reverse(go(this, NilL))
}
//end::init[]

class Solution_5_1 : WordSpec({
    "Stream.toList" should {
        "force the stream into an evaluated list" {
            val s = Stream.of(1, 2, 3, 4, 5)
            s.toListUnsf() shouldBe List.of(1, 2, 3, 4, 5)
            s.toList() shouldBe List.of(1, 2, 3, 4, 5)
        }
    }
})