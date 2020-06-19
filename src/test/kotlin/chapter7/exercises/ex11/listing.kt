package chapter7.exercises.ex11

import kotlinx.collections.immutable.PersistentMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

typealias Par<A> = (ExecutorService) -> Future<A>

//tag::init[]
fun <A, B> flatMap(pa: Par<A>, f: (A) -> Par<B>): Par<B> = { es ->
    f(pa(es).get())(es)
}
//end::init[]

fun <A> choice(cond: Par<Boolean>, t: Par<A>, f: Par<A>): Par<A> =
    { es: ExecutorService ->
        flatMap(cond, { if (it) t else f })(es)
    }

fun <A> choiceN(n: Par<Int>, choices: List<Par<A>>): Par<A> =
    { es: ExecutorService ->
        flatMap(n, { choices[it] })(es)
    }

fun <K, V> choiceMap(
    key: Par<K>,
    choices: PersistentMap<K, Par<V>>
): Par<V> =
    { es: ExecutorService ->
        flatMap(key, { choices.getValue(it) })(es)
    }