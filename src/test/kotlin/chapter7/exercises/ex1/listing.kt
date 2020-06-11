package chapter7.exercises.ex1

interface Par<A> {
    fun get(): A
}

fun <A, B, C> map2(pa: Par<A>, pb: Par<B>, f: (A, B) -> C): Par<C> = TODO()
