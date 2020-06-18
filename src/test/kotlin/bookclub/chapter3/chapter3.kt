package bookclub.chapter3

import chapter3.Cons
import chapter3.List
import chapter3.Nil

///////////////////////////////////////////////////////////////////////////

fun <A, B> map(xs: List<A>, f: (A) -> B): List<B> =
    when (xs) {
        is Nil -> xs
        is Cons -> Cons(
            f(xs.head),
            map(xs.tail, f)
        )
    }

fun <A> reduce(xs: List<A>, f: (A, A) -> A): A =
    when (xs) {
        is Nil -> throw RuntimeException("Cannot reduce empty list")
        is Cons -> when (xs.tail) {
            is Nil -> xs.head
            is Cons -> f(
                xs.head,
                reduce(xs.tail, f)
            )
        }
    }

///////////////////////////////////////////////////////////////////////////
/*
    fun <A, B> foldRight(
        xs: List<A>, 
        z: B, 
        f: (A, B) -> B
    ): B

    fun <A> reduce(
        xs: List<B>, 
        g: (B, B) -> B
    ): B

    h: (A) -> B
    g: (B, B) -> B

    f: (A, B) -> B = {a, b -> g(h(a), b)}

*/
///////////////////////////////////////////////////////////////////////////

fun toLetter(i: Int) =
    (i % ('z'.toInt() - 'a'.toInt()) + 'a'.toInt()).toChar().toString()

fun concat(s1: String, s2: String) = s1 + s2

fun prependLetterTo(i: Int, s: String): String = concat(toLetter(i), s)

// region fun helpers
// See https://github.com/MarioAriasC/funKTionale
fun <P1, P2, R> ((P1, P2) -> R).curried(): (P1) -> (P2) -> R {
    return { p1: P1 -> { p2: P2 -> this(p1, p2) } }
}

fun <P1, P2, R> ((P1) -> (P2) -> R).uncurried(): (P1, P2) -> R {
    return { p1: P1, p2: P2 -> this(p1)(p2) }
}

infix fun <IP, R, P1> ((IP) -> R).compose_(f: (P1) -> IP): (P1) -> R {
    return { p1: P1 -> this(f(p1)) }
}
// endregion fun helpers

val prependLetterTo: (Int, String) -> String =
    ::concat.curried().compose_(::toLetter).uncurried()

///////////////////////////////////////////////////////////////////////////
/*

    fun <A, B> foldRight(
        xs: List<A>,
        z: B, 
        f: (A, B) -> B
    ): B

    fun <A, B> foldRight(
        xs: List<A>,
        z: B, 
        h: (A) -> B,
        g: (B, B) -> B
    ): B

    reduce(
        append(
            map(xs, h),
            z
        ),
        g
    )
*/

///////////////////////////////////////////////////////////////////////////
/*

 Excursion: Type class "Monoid"

 **Type classes** are _interfaces_
 that define a set of generic _extension functions_
 associated to a _type_.

 You may see them referred to as “extension interfaces.”

 See-more: https://arrow-kt.io/docs/0.10/typeclasses/intro/
*/

///////////////////////////////////////////////////////////////////////////

/**
 * Simplified Monoid interface
 *
 * @param A - Anything
 *
 * @param F - container of any kind: List<A>, Tree<A>, Option<A>, etc.
 *            List<A> is used just as a minimal compilable example
 *
 * See-more: https://github.com/arrow-kt/arrow-core/blob/master/arrow-core-data/src/main/kotlin/arrow/typeclasses/MonoidK.kt
 */
interface Monoid<F, A> where F : List<A> {
    fun empty(): A

    fun combine(f: F): A
}

///////////////////////////////////////////////////////////////////////////

object SumListOfIntMonoid : Monoid<List<Int>, Int> {
    override fun empty(): Int = 0
    override fun combine(f: List<Int>): Int = reduce(f, Int::plus) //*
    // * should be fold(f, empty(), Int::plus)
}

object ProductListOfIntMonoid : Monoid<List<Int>, Int> {
    override fun empty(): Int = 1
    override fun combine(f: List<Int>): Int = reduce(f, Int::times)
}

// fun main() {
//     val list = List.of(1, 2, 3, 4, 5)
//
//     println(SumListOfIntMonoid.combine(list))
//
//     println(ProductListOfIntMonoid.combine(list))
// }

///////////////////////////////////////////////////////////////////////////

typealias Fun<A> = (A) -> A

// fun <A> foo(a: A): A = TODO()

fun plus(i: Int): Int = i + 1

fun <A> identity(a: A): A = a

fun <C, D, E> compose(
    f: (D) -> E,
    g: (C) -> D
): (C) -> E = { c: C -> f(g(c)) }

///////////////////////////////////////////////////////////////////////////

class ComposeListOfFunMonoid<A> : Monoid<List<Fun<A>>, Fun<A>> {

    override fun empty(): Fun<A> = ::identity

    override fun combine(f: List<Fun<A>>): Fun<A> = reduce(f, ::compose)
}

// /*
// * List(times1, times2, times3, times4, times5)
// * fun times120(x:Int) = times5(times4(times3(times2(times1(x)))))
// *
// * */
// fun main() {
//     val list = List.of(1, 2, 3, 4, 5)
//
//     fun times(x: Int) = { y: Int -> x * y }
//
//     val times5 = times(5)
//     println("times5(3) = ${times5(3)}") // times5(3) = 15
//
//     val funList = map(list, ::times)
//     // List(::times1,::times2,::times3,::times4,::times5)
//
//     val times120 =
//         ComposeListOfFunMonoid<Int>().combine(funList)
//
//     println("times120(2) = ${times120(2)}")
//
//     //times120(1) = 120
// }

///////////////////////////////////////////////////////////////////////////
/*
*
    fun <A, B> foldRight(
        xs: List<A>,
        z: B,
        f: (A, B) -> B
    ): B

    fun <A, B> foldRight(
        xs: List<A>,
        z: B,
        h: (A) -> B,
        g: (B, B) -> B
    ): B

    reduce(
        append(
            map(xs, h),
            z
        ),
        g
    )*/
fun <A, B> foldRight(xs: List<A>, z: B, f: (A, B) -> B): B {

    fun partiallyF(a: A) = { b: B -> f(a, b) } // f.curried()(a)

    val funs = map(xs, ::partiallyF)

    val combined =
        ComposeListOfFunMonoid<B>().combine(funs)

    return combined(z)
}

///////////////////////////////////////////////////////////////////////////

fun <A> cons(head: A, tail: List<A>): List<A> = Cons(head, tail)

// fun main() {
//     val list = List.of(1, 2, 3, 4, 5)
//
//     println(
//         foldRight(list, 1, Int::times)
//     )
//
//     println(
//         foldRight(
//             list,
//             List.empty(),
//             ::cons
//         )
//     )
// }

///////////////////////////////////////////////////////////////////////////

/*
fun <A, B> foldLeftR(xs: List<A>, z: B, f: (B, A) -> B): B =
    foldRight(
        xs,
        { b: B -> b },
        { a: A, bb: (B) -> B ->
            { b: B -> bb(f(b, a)) }
        })(z)
 */

fun <A, B> foldLeft(xs: List<A>, z: B, f: (B, A) -> B): B {
    fun partiallyF(a: A) = { b: B -> f(b, a) }
    val combined =
        foldRight<A, (B) -> B>(
            xs,
            ::identity,
            { a, bFun ->
                bFun.compose_(partiallyF(a))
                // same as `compose(bFun, partiallyF(a))`
                // see what happens if you reverse the arguments
            }
        )

    return combined(z)
/*
// combined = { b ->
//   identity(
//     partiallyFN(
//     ...
//         partiallyF3(
//             partiallyF2(
//                 partiallyF1(
//                     b
//                 )
//             )
//         )
//     ...
//     )
//  )
// }
 */
}
//
// fun main() {
//     val list = List.of(1, 2, 3, 4, 5)
//     println(
//         foldLeft<Int, List<Int>>(
//             list,
//             List.empty(),
//             { tail, head -> Cons(head, tail) })
//     )
// }
