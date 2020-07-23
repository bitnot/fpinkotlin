package chapter9.exercises.ex5

interface Parser<A>

fun <A, B, C> map2(
    pa: Parser<A>,
    pb: Parser<B>,
    f: (A, B) -> C
): Parser<C> = TODO()

infix fun <A> Parser<out A>.or(p: Parser<out A>): Parser<A> = TODO()

fun <A> succeed(a: A): Parser<A> = TODO()

fun <A> defer(pa: () -> Parser<A>): Parser<A> = TODO()

fun <A> many(pa: Parser<A>): Parser<List<A>> =
    map2(pa, defer { many(pa) }) { a, la ->
        listOf(a) + la
    } or succeed(emptyList())
