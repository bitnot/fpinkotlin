package chapter9.exercises.ex3

import chapter9.solutions.final.ParseError
import chapter9.solutions.final.Parser
import chapter9.solutions.final.ParserDsl

abstract class Listing : ParserDsl<ParseError>() {

    init {
        //tag::init1[]
        fun <A> many(pa: Parser<A>): Parser<List<A>> =
            this.map2(pa, { many(pa) }) { head, tail ->
                listOf(head) + tail
            } or succeed(emptyList())
        //end::init1[]
    }
}
