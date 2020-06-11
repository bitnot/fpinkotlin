package chapter7.exercises.ex5

import chapter7.solutions.sol3.map2
import chapter7.solutions.sol4.Par
import chapter7.solutions.sol4.unit

//tag::init1[]
fun <A> sequence(ps: List<Par<A>>): Par<List<A>> =
    ps.foldRight(unit(listOf())) { pa: Par<A>, pb: Par<List<A>> ->
        map2(pa, pb) { a, b ->
            listOf(a) + b
        }
    }
//end::init1[]
