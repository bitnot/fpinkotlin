package chapter6.exercises

import arrow.core.Id
import arrow.core.Tuple2
import arrow.core.extensions.id.monad.monad
import arrow.core.extensions.list.foldable.foldLeft
import arrow.mtl.State
import arrow.mtl.StateApi
import arrow.mtl.extensions.fx
import arrow.mtl.runS
import arrow.mtl.stateSequential
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

val update: (Input) -> (Machine) -> Machine =
    { input: Input ->
        { machine: Machine ->
            println("Updating $machine with $input")
            if (input == Coin) {
                if (!machine.locked || machine.candies <= 0) machine
                else Machine(false, machine.candies, machine.coins + 1)
            } else {
                if (machine.locked || machine.candies <= 0) machine
                else Machine(true, machine.candies - 1, machine.coins)
            }
        }
    }

fun update1(machine: Machine, input: Input): Machine =
    update(input)(machine)
//end::init1[]

//tag::init2[]
fun simulateMachine1(
    inputs: List<Input>
): State<Machine, Unit> = State({ initialMachine ->
    val updatedMachine =
        inputs.foldLeft(initialMachine, ::update1)
    Tuple2(updatedMachine, Unit)
})

fun simulateMachine2(
    inputs: List<Input>
): State<Machine, Unit> = State.fx(Id.monad()) {
    val transitions = inputs.map(update)
    val statements = transitions.map(StateApi::modify)
    val composedStatement = statements.stateSequential()
    composedStatement.bind()
}

fun simulateMachine3(
    inputs: List<Input>
): State<Machine, Unit> =
    State.fx(Id.monad()) {
        val transitions = inputs.map(update)
        val states = transitions.map(StateApi::modify)
        val finalState = states.stateSequential()
        val (whatever) = finalState
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
