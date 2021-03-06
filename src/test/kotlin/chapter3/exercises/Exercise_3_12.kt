package chapter3.exercises

import chapter3.Cons
import chapter3.List
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
fun <A, B> foldLeftR(xs: List<A>, z: B, f: (B, A) -> B): B =
    foldRight(
        xs,
        { b: B -> b },
        { a, bb ->
            { b -> bb(f(b, a)) }
        })(z)

fun <A, B> foldRightL(xs: List<A>, z: B, f: (A, B) -> B): B {
    val reversed = foldLeft(xs, List.empty<A>(), { b, a -> Cons(a, b) })
    return foldLeft(reversed, z, { b, a -> f(a, b) })
}
// end::init[]

class Exercise_3_12 : WordSpec({
    "list foldLeftR" should {
        "implement foldLeft functionality using foldRight" {
            foldLeftR(
                List.of(1, 2, 3, 4, 5),
                0,
                { x, y -> x + y }) shouldBe 15
        }

        "reverse list elements" {
            foldLeftR(
                List.of(1, 2, 3, 4, 5),
                List.empty<Int>(),
                { b, a -> Cons(a, b) }) shouldBe
                List.of(5, 4, 3, 2, 1)
        }
    }

    "list foldRightL" should {
        "implement foldRight functionality using foldLeft" {
            foldRightL(
                List.of(1, 2, 3, 4, 5),
                0,
                { x, y -> x + y }) shouldBe 15
        }

        "keep list elements in the same order" {
            foldRightL(
                List.of(1, 2, 3, 4, 5),
                List.empty<Int>(),
                { a, b -> Cons(a, b) }) shouldBe
                List.of(1, 2, 3, 4, 5)
        }
    }
})
