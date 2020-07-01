package chapter7.exercises.ex10

import chapter7.solutions.sol10.Par
import chapter7.solutions.sol5.Pars.map
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

typealias Par<A> = (ExecutorService) -> Future<A>

//tag::init[]
fun <K, V> choiceMap(key: Par<K>, choices: Map<K, Par<V>>): Par<V> =
    { es: ExecutorService ->
        (map(key) { k -> choices.getValue(k)(es) }).invoke(es).get()
    }

//end::init[]
