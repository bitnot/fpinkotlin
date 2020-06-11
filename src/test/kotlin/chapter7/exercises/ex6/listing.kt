package chapter7.exercises.ex6

import chapter7.sec4_4.Par
import chapter7.sec4_4.map2
import chapter7.sec4_4.parMap
import chapter7.sec4_4.unit

//tag::init[]
fun <A> parFilter(sa: List<A>, f: (A) -> Boolean): Par<List<A>> {
    val filtered = parMap(sa) { a -> if (f(a)) listOf(a) else emptyList() }
    return map2(filtered, unit(Unit)) { xs, _ -> xs.flatten() }
}

//end::init[]
