package chapter5.exercises

import chapter4.Some
import chapter5.Stream
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class Exercise_5_14 : WordSpec({

    //tag::startswith[]
    fun <A> Stream<A>.startsWith(that: Stream<A>): Boolean = this
        .zipAll(that)
        .takeWhile { (_, o2) -> o2 is Some }
        .forAll { (o1, o2) -> o1 == o2 }
    //end::startswith[]

    "Stream.startsWith" should {
        "detect if one stream is a prefix of another" {
            Stream.of(1, 2, 3).startsWith(
                Stream.of(1, 2)
            ) shouldBe true
        }
        "detect if one stream is not a prefix of another" {
            Stream.of(1, 2, 3).startsWith(
                Stream.of(2, 3)
            ) shouldBe false
        }
    }
})
