package chapter5.exercises

import chapter3.List
import chapter4.None
import chapter4.Option
import chapter4.Some
import chapter4.exercises.flatMap
import chapter4.exercises.getOrElse
import chapter5.Boilerplate.foldRight
import chapter5.Cons
import chapter5.Empty
import chapter5.Stream
import chapter5.Stream.Companion.cons
import chapter5.solutions.toList
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class Exercise_5_16 : WordSpec({

    fun <A> Stream<A>.headOption(): Option<A> =
        this.foldRight<A, Option<A>>({ None }, { a, _ -> Some(a) })

    //tag::scanright[]
    fun <A, B> Stream<A>.scanRight(z: B, f: (A, () -> B) -> B): Stream<B> =
        this.foldRight({ Stream.of(z) },
            { a, b ->
                cons({ f(a, { b().headOption().getOrElse { z } }) }, b)
            })

    fun <A, B> Stream<A>.scanRightU(
        z: B,
        f: (A, () -> B) -> B
    ): Stream<B> =
        unfold(Option.of(this), {
            it.flatMap {
                when (it) {
                    is Empty -> Some(Pair(z, None))
                    is Cons -> {
                        val tail = it.t()
                        val next = tail.scanRight(z, f)
                        when (next) {
                            is Empty -> Some(
                                Pair(
                                    z, Option.of(tail)
                                )
                            )
                            is Cons -> Some(
                                Pair(
                                    f(it.h(), next.h),
                                    Option.of(tail)
                                )
                            )
                        }
                    }
                }
            }
        })
    //end::scanright[]

    "Stream.scanRight" should {
        "behave like foldRight" {
            Stream.of(1, 2, 3)
                .scanRight(0, { a, b ->
                    a + b()
                }).toList() shouldBe List.of(6, 5, 3, 0)
        }
    }
    "Stream.scanRightU" should {
        "behave like foldRight" {
            Stream.of(1, 2, 3)
                .scanRightU(0, { a, b ->
                    a + b()
                }).toList() shouldBe List.of(6, 5, 3, 0)
        }
    }
})
