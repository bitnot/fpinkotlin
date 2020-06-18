# 3 Functional data structures

~~## 3.1  Defining functional data structures~~

~~## 3.2  Working with functional data structures~~

## 3.3  Data sharing in functional data structures


#### Exercise [3.1]

Implement the function `tail` for removing the first element of a `List`. Note that the function takes constant time. What are different choices you could make in your implementation if the `List` is `Nil`? We’ll return to this question in the next chapter.


#### Exercise [3.2]

Using the same idea, implement the function `setHead` for replacing the first element of a `List` with a different value.


#### Exercise [3.3]

Generalize `tail` to the function `drop`, which removes the first `n` elements from a list. Note that this function takes time proportional only to the number of elements being dropped—we don’t need to make a copy of the entire `List`.


#### Exercise [3.4]

Implement `dropWhile`, which removes elements from the `List` prefix as long as they match a predicate.


### Exercise [3.5]

Not everything works out so nicely as when we append two lists to each other. Implement a function, `init`, that returns a `List` consisting of all but the last element of a `List`. So, given `List(1, 2, 3, 4)`, `init` will return `List(1, 2, 3)`. Why can’t this function be implemented in constant time like `tail`?


## 3.4  Recursion over lists and generalizing to higher-order functions

#### Exercise [3.6]

Can `product`, implemented using `foldRight`, immediately halt the recursion and return `0.0` if it encounters a `0.0`? Why or why not? Consider how any short-circuiting might work if you call `foldRight` with a large list. This question has deeper implications that we will return to in chapter 5.


#### Exercise [3.7]

See what happens when you pass `Nil` and `Cons` themselves to `foldRight`, like this:
`foldRight(List.of(1, 2, 3), List.empty<Int>(), { x, y -> Cons(x, y) })`.
What do you think this says about the relationship between `foldRight` and the data constructors of `List`?


####Exercise [3.8]

Compute the length of a list using `foldRight`.


#### Exercise [3.9]

Our implementation of foldRight is not tail-recursive and will result in a StackOverflowError for large lists (we say it’s not stack-safe). Convince yourself that this is the case, and then write another general list-recursion function, foldLeft, that is tail-recursive, using the techniques we discussed in the previous chapter. Here is its signature:
`tailrec fun <A, B> foldLeft(xs: List<A>, z: B, f: (B, A) -> B): B = TODO()`

------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------


#### Exercise [3.10]

Write `sum`, `product`, and a function to compute the length of a list using `foldLeft`.


#### Exercise [3.11]

Write a function that returns the reverse of a list (given `List(1,2,3) `it returns `List(3,2,1)`). See if you can write it using a `fold`.


#### Exercise [3.12]

Hard: Can you write `foldLeft` in terms of `foldRight`? 
How about the other way around? 

Implementing `foldRight` via `foldLeft` is useful because it lets us implement `foldRight` tail-recursively, 
which means it works even for large lists without overflowing the stack.


#### Exercise [3.13]

Implement `append` in terms of either `foldLeft` or `foldRight`.


#### Exercise [3.14]

Hard: Write a function that concatenates a list of lists into a single list. 

Its runtime should be linear in the total length of all lists. Try to use functions we have already defined.


### 3.4.1  More functions for working with lists


#### Exercise [3.15]

Write a function that transforms a list of integers by adding `1` to each element. 
This should be a pure function that returns a new `List`.


#### Exercise [3.16]

Write a function that turns each value in a `List<Double>` into a `String`. 
You can use the expression `d.toString()` to convert some `d: Double` to a `String`.


#### Exercise [3.17]

Write a function `map` that generalizes modifying each element in a list while maintaining the structure of the list. 
Here is its signature: 

`fun <A, B> map(xs: List<A>, f: (A) -> B): List<B> = TODO()`.


#### Exercise [3.18]

Write a function `filter` that removes elements from a list unless they satisfy a given predicate. 
Use it to remove all odd numbers from a `List<Int>`.


#### Exercise [3.19]
 Write a function `flatMap` that works like map except that the function given will return a list 
 instead of a single result, and that list should be inserted into the final resulting list. 
 Here is its signature:
 `fun <A, B> flatMap(xa: List<A>, f: (A) -> List<B>): List<B> = TODO()`


#### Exercise [3.20]
 Use `flatMap` to implement `filter`.


#### Exercise [3.21]
 Write a function that accepts two lists and constructs a new list by adding corresponding elements. 
 For example, `List(1,2,3)` and `List(4,5,6)` become `List(5,7,9)`.


#### Exercise [3.22]
 Generalize the function you just wrote so that it’s not specific to integers or addition. 
 Name your generalized function `zipWith`.


~~### 3.4.2  Lists in the Kotlin standard library~~

### 3.4.3  Inefficiency on assembling list functions from simpler components


#### Exercise [3.23]

Hard: As an example, implement `hasSubsequence` for checking whether a List contains another `List` as a subsequence. 
For instance, `List(1,2,3,4)` would have `List(1,2)`, `List(2,3)`, and `List(4)` as subsequences, among others. 
You may have some difficulty finding a concise purely functional implementation that is also efficient. That’s okay. 
Implement the function however comes most naturally. 
We’ll return to this implementation in chapter 5 and hopefully improve on it.


## 3.5  Trees


#### Exercise [3.24]

Write a function `size` that counts the number of nodes (leaves and branches) in a tree.


#### Exercise [3.25]

Write a function `maximum` that returns the maximum element in a `Tree<Int>`.


#### Exercise [3.26]

Write a function `depth` that returns the maximum path length from the root of a tree to any leaf.


#### Exercise [3.27]

Write a function `map`, analogous to the method of the same name on `List`, that modifies each element in a tree with a given function.


#### Exercise [3.28]

Generalize `size`, `maximum`, `depth`, and `map` for `Tree`, writing a new function `fold` that abstracts over their similarities. Reimplement them in terms of this more general function. Can you draw an analogy between this `fold` function and the left and right folds for `List`?


[3.1]: ../../chapter3/exercises/Exercise_3_1.kt
[3.2]: ../../chapter3/exercises/Exercise_3_2.kt
[3.3]: ../../chapter3/exercises/Exercise_3_3.kt
[3.4]: ../../chapter3/exercises/Exercise_3_4.kt
[3.5]: ../../chapter3/exercises/Exercise_3_5.kt
[3.6]: ../../chapter3/exercises/Exercise_3_6.kt
[3.7]: ../../chapter3/exercises/Exercise_3_7.kt
[3.8]: ../../chapter3/exercises/Exercise_3_8.kt
[3.9]: ../../chapter3/exercises/Exercise_3_9.kt
[3.10]: ../../chapter3/exercises/Exercise_3_10.kt
[3.11]: ../../chapter3/exercises/Exercise_3_11.kt
[3.12]: ../../chapter3/exercises/Exercise_3_12.kt
[3.13]: ../../chapter3/exercises/Exercise_3_13.kt
[3.14]: ../../chapter3/exercises/Exercise_3_14.kt
[3.15]: ../../chapter3/exercises/Exercise_3_15.kt
[3.16]: ../../chapter3/exercises/Exercise_3_16.kt
[3.17]: ../../chapter3/exercises/Exercise_3_17.kt
[3.18]: ../../chapter3/exercises/Exercise_3_18.kt
[3.19]: ../../chapter3/exercises/Exercise_3_19.kt
[3.20]: ../../chapter3/exercises/Exercise_3_20.kt
[3.21]: ../../chapter3/exercises/Exercise_3_21.kt
[3.22]: ../../chapter3/exercises/Exercise_3_22.kt
[3.23]: ../../chapter3/exercises/Exercise_3_23.kt
[3.24]: ../../chapter3/exercises/Exercise_3_24.kt
[3.25]: ../../chapter3/exercises/Exercise_3_25.kt
[3.26]: ../../chapter3/exercises/Exercise_3_26.kt
[3.27]: ../../chapter3/exercises/Exercise_3_27.kt
[3.28]: ../../chapter3/exercises/Exercise_3_28.kt
