package chapter4

//tag::init[]
sealed class Option<out A> {
    //helper functions in companion object
    //tag::helpers[]
    companion object {
        fun <A> empty(): Option<A> = None
        fun <A> of(a: A): Option<A> = Some(a)
    }
    //end::helpers[]
}

data class Some<out A>(val get: A) : Option<A>()
object None : Option<Nothing>()
//end::init[]
