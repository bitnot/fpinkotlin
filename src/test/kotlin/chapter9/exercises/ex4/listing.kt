package chapter9.exercises.ex4

import chapter9.solutions.final.ParseError
import chapter9.solutions.final.Parser
import chapter9.solutions.final.ParserDsl

abstract class Listing : ParserDsl<ParseError>() {

    init {
        fun <A> cons(head: A, tail: List<A>): List<A> = listOf(head) + tail

        //tag::init1[]
        fun <A> listOfN(n: Int, pa: Parser<A>): Parser<List<A>> =
            if (n <= 0) succeed(emptyList())
            else map2(pa, { listOfN(n - 1, pa) }, ::cons)
        //end::init1[]
    }
}
