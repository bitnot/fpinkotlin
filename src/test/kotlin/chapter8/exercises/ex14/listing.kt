package chapter8.exercises.ex14

import arrow.core.extensions.list.foldable.exists
import chapter8.sec3.listing3.Gen
import chapter8.sec3.listing3.Prop
import chapter8.sec3.listing3.SGen
import chapter8.sec4.listing1.run

class Ex14 {
    val smallInt = Gen.choose(-10, 10)

    fun List<Int>.prepend(i: Int) = listOf(i) + this

    val maxProp: Prop =
        Prop.Companion.forAll(SGen.listOf(smallInt)) { ns ->
            val sorted = ns.sorted()
            sorted.isEmpty()
                || sorted.first() == sorted.min()
                && sorted.reversed().first() == sorted.max()
        }
}

fun main() {
    run(Ex14().maxProp)
}
