package chapter6.exercises

import arrow.core.Id
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.extensions.id.monad.monad
import arrow.core.extensions.list.foldable.combineAll
import arrow.core.extensions.list.foldable.foldLeft
import arrow.core.toT
import arrow.mtl.State
import arrow.mtl.StateApi
import arrow.mtl.extensions.fx
import arrow.mtl.runS
import arrow.mtl.stateSequential
import arrow.typeclasses.Monoid
import arrow.typeclasses.Semigroup
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import kotlinx.collections.immutable.persistentListOf

//tag::init1[]
sealed class Input

object Coin : Input()
object Turn : Input()

data class Machine(
    val locked: Boolean,
    val candies: Int,
    val coins: Int
)

//end::init1[]

//tag::init2[]
/*
* Option 1: Simply fold the inputs, no monad-comprehension
* */
fun update(machine: Machine, input: Input): Machine =
    updateCurried(input)(machine)

fun simulateMachine1(
    inputs: List<Input>
): State<Machine, Unit> = State { initialMachine ->
    val updatedMachine = inputs.foldLeft(initialMachine, ::update)
    updatedMachine toT Unit
}

/*
* Option 2: Map inputs to Machine transition functions, then to State-s,
* then do `stateSequential`
* */
val updateCurried: (Input) -> (Machine) -> Machine =
    { input: Input ->
        { machine: Machine ->
            println("Updating $machine with $input")
            when (input) {
                Coin -> {
                    if (!machine.locked || machine.candies <= 0) machine
                    else Machine(false, machine.candies, machine.coins + 1)
                }
                Turn -> {
                    if (machine.locked || machine.candies <= 0) machine
                    else Machine(true, machine.candies - 1, machine.coins)
                }
            }
        }
    }

fun simulateMachine2(
    inputs: List<Input>
): State<Machine, Unit> =
    State.fx(Id.monad()) {
        val transitions = inputs.map(updateCurried)
        val statements = transitions.map(StateApi::modify)
        val program = statements.stateSequential()
        val (whatever) = program
    }

fun simulateMachine2nope(
    inputs: List<Input>
): State<Machine, Unit> =
    State.fx(Id.monad()) {
        val transitions = inputs.map(updateCurried)
        val statements = transitions.map(StateApi::modify)
        val program = statements.stateSequential()
        // Why does it not continue like `simulateMachine2`?
        val (_) = program
    }

fun simulateMachine2a(
    inputs: List<Input>
): State<Machine, Unit> = State.fx(Id.monad()) {
    val transitions = inputs.map(updateCurried)
    val statements = transitions.map(StateApi::modify)
    val program = statements.stateSequential()
    program.bind()
}

fun simulateMachine2b(
    inputs: List<Input>
): State<Machine, Unit> {
    val transitions = inputs.map(updateCurried)
    val statements = transitions.map(StateApi::modify)
    val program = statements.stateSequential()
    return program.map(Id.monad()) { _ -> Unit }
}

/*
* Option 4: Map inputs to State-s and then combine all with a Monoid
* */
object Candy

fun insertCoin() = State<Machine, Option<Candy>> { machine ->
    val result = if (!machine.locked || machine.candies <= 0) machine
    else Machine(false, machine.candies, machine.coins + 1)
    result toT None
}

fun rotateKnob() = State<Machine, Option<Candy>> { machine ->
    if (machine.locked || machine.candies <= 0) machine toT None
    else Machine(true, machine.candies - 1, machine.coins) toT Some(Candy)
}

fun toMachineTransition(input: Input) = when (input) {
    Coin -> insertCoin()
    Turn -> rotateKnob()
}.map(Id.monad()) { _ -> Unit }

interface MachineStateSemigroup : Semigroup<State<Machine, Unit>> {
    override fun State<Machine, Unit>.combine(
        other: State<Machine, Unit>
    ): State<Machine, Unit> = State { machine ->
        other.runS(this.runS(machine)) toT Unit
    }
}

interface MachineStateMonoid : Monoid<State<Machine, Unit>>,
    MachineStateSemigroup {
    override fun empty(): State<Machine, Unit> =
        State { machine ->
            machine toT Unit
        }
}

fun simulateMachine4(
    inputs: List<Input>
): State<Machine, Unit> {
    val statements = inputs.map(::toMachineTransition)
    val program = statements.combineAll(object : MachineStateMonoid {})
    return State.fx(Id.monad()) {
        val (foo) = program
    }
}

val simulateMachine = ::simulateMachine2
//end::init2[]

class Solution_6_11 : WordSpec({
    "simulateMachine" should {
        "allow the purchase of a single candy" {
            val actions = persistentListOf(Coin)
            val before =
                Machine(locked = true, candies = 1, coins = 0)
            val after =
                Machine(locked = false, candies = 1, coins = 1)
            simulateMachine(actions).runS(before) shouldBe after
        }
        "allow the redemption of a single candy" {
            val actions = persistentListOf(Turn)
            val before =
                Machine(locked = false, candies = 1, coins = 1)
            val after = Machine(locked = true, candies = 0, coins = 1)
            simulateMachine(actions).runS(before) shouldBe after
        }
        "allow purchase and redemption of a candy" {
            val actions = persistentListOf(Coin, Turn)
            val before =
                Machine(locked = true, candies = 1, coins = 0)
            val after = Machine(locked = true, candies = 0, coins = 1)
            simulateMachine(actions).runS(before) shouldBe after
        }
    }

    "inserting a coin into a locked machine" should {
        "unlock the machine if there is some candy" {
            val actions = persistentListOf(Coin)
            val before =
                Machine(locked = true, candies = 1, coins = 0)
            val after =
                Machine(locked = false, candies = 1, coins = 1)
            simulateMachine(actions).runS(before) shouldBe after
        }
    }
    "inserting a coin into an unlocked machine" should {
        "do nothing" {
            val actions = persistentListOf(Coin)
            val before =
                Machine(locked = false, candies = 1, coins = 1)
            val after =
                Machine(locked = false, candies = 1, coins = 1)
            simulateMachine(actions).runS(before) shouldBe after
        }
    }
    "turning the knob on an unlocked machine" should {
        "cause it to dispense candy and lock" {
            val actions = persistentListOf(Turn)
            val before =
                Machine(locked = false, candies = 1, coins = 1)
            val after = Machine(locked = true, candies = 0, coins = 1)
            simulateMachine(actions).runS(before) shouldBe after
        }
    }
    "turning the knob on a locked machine" should {
        "do nothing" {
            val actions = persistentListOf(Turn)
            val before =
                Machine(locked = true, candies = 1, coins = 1)
            val after = Machine(locked = true, candies = 1, coins = 1)
            simulateMachine(actions).runS(before) shouldBe after
        }
    }
    "a machine that is out of candy" should {
        "ignore the turn of a knob" {
            val actions = persistentListOf(Turn)
            val before =
                Machine(locked = true, candies = 0, coins = 0)
            val after = Machine(locked = true, candies = 0, coins = 0)
            simulateMachine(actions).runS(before) shouldBe after
        }
        "ignore the insertion of a coin" {
            val actions = persistentListOf(Coin)
            val before =
                Machine(locked = true, candies = 0, coins = 0)
            val after = Machine(locked = true, candies = 0, coins = 0)
            simulateMachine(actions).runS(before) shouldBe after
        }
    }
})
