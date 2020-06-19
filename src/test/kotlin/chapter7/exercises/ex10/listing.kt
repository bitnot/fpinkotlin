package chapter7.exercises.ex10

import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

import chapter7.exercises.ex5.sequence
import chapter7.solutions.sol5.Pars.map
import chapter7.exercises.ex7.Pars.unit
import chapter7.solutions.sol3.map2
import chapter7.solutions.sol9.Par

typealias Par<A> = (ExecutorService) -> Future<A>

//tag::init[]
fun <K, V> choiceMap(key: Par<K>, choices: Map<K, Par<V>>): Par<V> =
    { es: ExecutorService ->
        (map(key) { k -> choices.getValue(k)(es) }).invoke(es).get()
    }

//end::init[]
