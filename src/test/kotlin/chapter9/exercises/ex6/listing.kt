package chapter9.exercises.ex6

import chapter9.solutions.final.ParseError
import chapter9.solutions.final.Parser
import chapter9.solutions.final.ParserDsl

abstract class Listing : ParserDsl<ParseError>() {
    init {

        //tag::init1[]
        val parser: Parser<Int> = regex("\\d+").flatMap { s ->
            val i = s.toInt()
            regex("a{${i}}").map { _ -> i }
        }
        //end::init1[]
    }
}
