# "FP in Kotlin" Book Club 24.04

## 3. Functional Data Structures

## Summary

### Immutability

- Immutable data structures support referential transparency and safe shared access without the need for copying.
- Recursion is used instead of loops with mutable variables

### Kotlin gotchas
- Kotlin standard library Lists are read-only, not immutable, 
  which could lead to data corruption when acted upon by pure functions.
- Kotlin `listOf` creates an `java.util.Arrays.ArrayList`

### `when` matching

- A sealed class has a finite amount of implementations, restricting data structure grammar.
- The `when` construct can match typed data structures, and is used for selecting an appropriate outcome evaluation.
- `when` matching on sealed classes can be statically checked for "totalness" 
- Kotlin matching is useful for working with data structures, but falls short of _pattern matching_ 
  supported by other functional languages.

### Algebraic Data Types

- Algebraic data types (ADTs) are the formal name of immutable data structures, and are modeled by data classes, 
  `Pair`s and `Triple`s in Kotlin.
- Both `List` and `Tree` that were developed in this chapter are examples of ADTs.


## Exercises

### Hard exercises

#### Exercise [3.12]

Can you write `foldLeft` in terms of `foldRight`? 
How about the other way around? 
Implementing `foldRight` via `foldLeft` is useful because it lets us implement `foldRight` tail-recursively, 
which means it works even for large lists without overflowing the stack.

_Runtime & memory complexity?_


#### Exercise [3.14]

Write a function that concatenates a list of lists into a single list. 
Its runtime should be linear in the total length of all lists. 
Try to use functions we have already defined.

_Runtime & memory complexity?_


#### Exercise [3.23]

Hard: As an example, implement `hasSubsequence` for checking whether a List contains another `List` as a subsequence. 
For instance, `List(1,2,3,4)` would have `List(1,2)`, `List(2,3)`, and `List(4)` as subsequences, among others. 
You may have some difficulty finding a concise purely functional implementation that is also efficient. That’s okay. 
Implement the function however comes most naturally. 
We’ll return to this implementation in chapter 5 and hopefully improve on it.

_Runtime & memory complexity?_


## Type classes

[arrow-kt intro to typeclasses](https://arrow-kt.io/docs/0.10/typeclasses/intro/)

> Typeclasses are interfaces that define a set of extension functions associated to one type. 
> You may see them referred to as “extension interfaces.”


### Foldable

[arrow-kt Foldable](https://arrow-kt.io/docs/0.10/arrow/typeclasses/foldable/)

The Typeclass `Foldable` provide us the ability, given a type `Kind<F, A>`, to aggregate their values `A`.

`Foldable<F>` is implemented in terms of two basic methods:

- `fa.foldLeft(init, f)` eagerly folds `fa` from left-to-right.
- `fa.foldRight(init, f)` lazily folds `fa` from right-to-left.



[3.12]: ../../chapter3/exercises/Exercise_3_12.kt
[3.14]: ../../chapter3/exercises/Exercise_3_14.kt
[3.23]: ../../chapter3/exercises/Exercise_3_23.kt
