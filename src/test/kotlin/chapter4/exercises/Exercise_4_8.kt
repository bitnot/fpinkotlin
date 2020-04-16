package chapter4.exercises

import chapter3.Cons
import chapter3.List
import chapter3.exercises.append
import chapter3.exercises.foldRight
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

sealed class Partial<out E, out A>

data class Failures<out E>(val get: List<E>) : Partial<E, Nothing>()
data class Success<out A>(val get: A) : Partial<Nothing, A>()

fun <E, A, B> Partial<E, A>.map(f: (A) -> B): Partial<E, B> =
    when (this) {
        is Failures -> this
        is Success -> Success(f(this.get))
    }

fun <E, A, B> Partial<E, A>.flatMap(
    f: (A) -> Partial<E, B>
): Partial<E, B> =
    when (this) {
        is Failures -> this
        is Success -> f(this.get)
    }

fun <E, A, B, C> map2(
    pa: Partial<E, A>,
    pb: Partial<E, B>,
    f: (A, B) -> C
): Partial<E, C> = when (pa) {
    is Failures -> when (pb) {
        is Failures -> Failures(append(pa.get, pb.get))
        is Success -> pa
    }
    is Success -> when (pb) {
        is Failures -> pb
        is Success -> Success(f(pa.get, pb.get))
    }
}

fun <E, A, B> traverse(
    xs: List<A>,
    f: (A) -> Partial<E, B>
): Partial<E, List<B>> = foldRight<A, Partial<E, List<B>>>(xs,
    Success(List.empty()),
    { h, t -> map2(f(h), t, { a, b -> Cons(a, b) }) })

fun <E, A> sequence(es: List<Partial<E, A>>): Partial<E, List<A>> =
    traverse(es, { it })

fun <A> catches(a: () -> A): Partial<String, A> =
    try {
        Success(a())
    } catch (e: Exception) {
        Failures(List.of(e.message!!))
    }

class Exercise_4_8 : WordSpec({
    "traverse" should {
        """return a Success either of a transformed list if all 
            transformations succeed""" {
            val xa = List.of("1", "2", "3", "4", "5")

            traverse(xa) { a ->
                catches { Integer.parseInt(a) }
            } shouldBe Success(List.of(1, 2, 3, 4, 5))
        }

        "return a Failures(List.of( either if any transformations fail" {
            val xa = List.of("1", "2", "x", "y", "4", "5")

            traverse(xa) { a ->
                catches { Integer.parseInt(a) }
            } shouldBe Failures(
                List.of(
                    """For input string: "x"""",
                    """For input string: "y""""
                )
            )
        }
    }
    "sequence" should {
        "turn a list of Success eithers into a Success either of list" {
            val xe: List<Partial<String, Int>> =
                List.of(Success(1), Success(2), Success(3))

            sequence(xe) shouldBe Success(List.of(1, 2, 3))
        }

        """convert a list containing any Failures(List.of( eithers into a 
            Failures(List.of( either""" {
            val xe: List<Partial<String, Int>> =
                List.of(
                    Success(1),
                    Failures(List.of("boom")),
                    Success(3)
                )

            sequence(xe) shouldBe Failures(List.of("boom"))
        }
    }
})
