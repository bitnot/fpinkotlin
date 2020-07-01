package chapter7.exercises.ex9

import chapter7.exercises.ex5.sequence
import chapter7.solutions.sol5.Pars.map
import chapter7.solutions.sol3.map2
import chapter7.solutions.sol10.Par

fun <A> choiceN(n: Par<Int>, choices: List<Par<A>>): Par<A> =
    map2(n, sequence(choices)) { i, xs -> xs[i] }

fun <A> choice(
    cond: Par<Boolean>,
    t: Par<A>,
    f: Par<A>
): Par<A> = choiceN(map(cond) { b -> if (b) 1 else 0 }, listOf(f, t))
