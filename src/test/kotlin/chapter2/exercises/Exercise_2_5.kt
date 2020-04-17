package chapter2.exercises

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class Exercise_2_5 : WordSpec({
    // tag::init[]
    fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C =
        { a -> f(g(a)) }
    // end::init[]

    "compose" should {
        "apply function composition over two functions" {
            val fahrenheit2celsius: (Double) -> String =
                compose(
                    { b: Double -> "$b degrees celsius" },
                    { a: Double -> (a - 32.0) * (5.0 / 9.0) }
                )

            fahrenheit2celsius(68.0) shouldBe "20.0 degrees celsius"
        }
    }
})
