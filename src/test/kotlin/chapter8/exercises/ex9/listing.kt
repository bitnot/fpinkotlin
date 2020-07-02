package chapter8.exercises.ex9

import chapter8.RNG

typealias TestCases = Int

sealed class Result {
    abstract fun isFalsified(): Boolean
}

object Passed : Result() {
    override fun isFalsified(): Boolean = false
}

typealias SuccessCount = Int
typealias FailedCase = String

data class Falsified(
    val failure: FailedCase,
    val successes: SuccessCount
) : Result() {
    override fun isFalsified(): Boolean = true
}

//tag::init[]
data class Prop(val run: (TestCases, RNG) -> Result) {
    fun and(p: Prop): Prop = Prop { testCases, rng ->
        val result = run(testCases, rng)
        if (result.isFalsified()) result
        else p.run(testCases, rng)
    }

    fun or(p: Prop): Prop = Prop { testCases, rng ->
        val result = run(testCases, rng)
        if (!result.isFalsified()) result
        else {
            val result2 = p.run(testCases, rng)
            if (result.isFalsified()) result
            else result2
        }
    }
}
//end::init[]
