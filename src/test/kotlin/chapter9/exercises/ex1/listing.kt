package chapter9.exercises.ex1

import chapter9.ParseError
import chapter9.solutions.final.Parser
import chapter9.solutions.final.ParserDsl

abstract class Listing : ParserDsl<ParseError>() {

    //tag::init1[]
    override fun <A, B, C> map2(
        pa: Parser<A>,
        pb: () -> Parser<B>,
        f: (A, B) -> C
    ): Parser<C> = product(pa, pb).map { f(it.first, it.second) }
    //end::init1[]

    //tag::init2[]
    override fun <A> many1(p: Parser<A>): Parser<List<A>> =
        map2(p, { many(p) }) { head, tail ->
            listOf(head) + tail
        }
    //end::init2[]
}
