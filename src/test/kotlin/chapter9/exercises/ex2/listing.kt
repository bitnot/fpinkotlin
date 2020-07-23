package chapter9.exercises.ex2

import chapter9.solutions.final.ParseError
import chapter9.solutions.final.Parser
import chapter9.solutions.final.ParserDsl

abstract class Listing : ParserDsl<ParseError>() {

    operator fun <A, B> Parser<A>.times(
        pb: Parser<B>
    ): Parser<Pair<A, B>> = product(this, { pb })

    infix fun <A, B> Parser<A>.equals(
        pb: Parser<B>
    ): Boolean = TODO()

    fun <A, B, C> commutativeLaw(
        pa: Parser<A>,
        pb: Parser<B>,
        pc: Parser<C>
    ) =
        ((pa * pb) * pc) equals (pa * (pb * pc))
}
