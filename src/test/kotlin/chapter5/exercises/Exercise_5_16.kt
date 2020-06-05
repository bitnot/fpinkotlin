package chapter5.exercises

import chapter3.List
import chapter4.None
import chapter4.Option
import chapter4.Some
import chapter4.exercises.getOrElse
import chapter4.solutions.flatMap
import chapter5.Boilerplate.foldRight
import chapter5.Cons
import chapter5.Empty
import chapter5.Stream
import chapter5.Stream.Companion.cons
import chapter5.solutions.toList
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/** Hard:
Generalize tails to the function scanRight, which is like a foldRight that returns a stream of the intermediate results.

For example:
>>> Stream.of(1, 2, 3).scanRight(0, { a, b -> a + b() }).toList()
res1: chapter3.List<kotlin.Int> = Cons(head=6, tail=Cons(head=5, tail=Cons(head=3, tail=Cons(head=

This example should be equivalent to the expression List.of(1+2+3+0, 2+3+0, 3+0, 0).

Your function should reuse intermediate results so that traversing a Stream with n elements always takes time linear in n.

Can it be implemented using unfold?
How, or why not?
Could it be implemented using another function we’ve written?

S                  A            Option<A,S>
Some(1,2,3)     -> 6 = 1+5  ->  Some(6 to Some(2,3))
Some(2,3  )     -> 5 = 2+3  ->  Some(5 to Some(3))
Some(3    )     -> 3 = 3+0  ->  Some(3 to Some(Empty))
Some(Empty)     -> 0 = 0    ->  Some(0 to None)
None            -> None     ->  None

 */
class Exercise_5_16 : WordSpec({

    fun <A> Stream<A>.headOption(): Option<A> =
        this.foldRight<A, Option<A>>({ None }, { a, _ -> Some(a) })

    //tag::scanright[]
    fun <A, B> Stream<A>.scanRightU0(
        z: B,
        f: (A, () -> B) -> B
    ): Stream<B> =
        unfold<B, Option<Stream<A>>>(Some(this), { it ->
            when (it) {
                is None -> None
                is Some<Stream<A>> -> when (val xs: Stream<A> = it.get) {
                    is Empty -> Some(z to None)
                    is Cons -> Some(
                        xs.foldRight({ z }, f) to Some(xs.t())
                    )
                }
            }
        })

    fun <A, B> Stream<A>.scanRight(z: B, f: (A, () -> B) -> B): Stream<B> =
        this.foldRight({ Stream.of(z) },
            { a, b: () -> Stream<B> ->
                cons({ f(a) { b().headOption().getOrElse { z } } }, b)
            })

    fun <A, B> Stream<A>.scanRightU(
        z: B,
        f: (A, () -> B) -> B
    ): Stream<B> =
        unfold(Option.of(this), {
            it.flatMap {
                when (it) {
                    is Empty -> Some(z to None)
                    is Cons -> {
                        val tail = it.t()
                        val next = tail.scanRight(z, f)
                        when (next) {
                            is Empty -> Some(z to Some(tail))
                            is Cons -> Some(
                                f(it.h(), next.h) to Some(tail)
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
    "Stream.scanRight1" should {
        "behave like foldRight" {
            Stream.of(1, 2, 3)
                .scanRightU0(0, { a, b ->
                    a + b()
                }).toList() shouldBe List.of(6, 5, 3, 0)
        }
    }
})
