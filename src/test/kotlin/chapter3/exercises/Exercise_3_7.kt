package chapter3.exercises

import chapter3.Cons
import chapter3.List
import chapter3.Nil

fun <A, B> foldRight(xs: List<A>, z: B, f: (A, B) -> B): B =
    when (xs) {
        is Nil -> z
        is Cons -> f(xs.head, foldRight(xs.tail, z, f))
    }

fun main() {
    // tag::init[]
    println(
        foldRight(
            List.of(1, 2, 3),
            List.empty<Int>(),
            { x, y -> Cons(x, y) })
    )
    // end::init[]
}

val f = { x: Int, y: List<Int> -> Cons(x, y) }
val z = Nil as List<Int>

val trace = {
    foldRight(List.of(1, 2, 3), z, f)
    Cons(1, foldRight(List.of(2, 3), z, f))
    Cons(1, Cons(2, foldRight(List.of(3), z, f)))
    Cons(1, Cons(2, Cons(3, foldRight(List.empty(), z, f))))
    Cons(1, Cons(2, Cons(3, Nil)))
}
