Journey through the looking glass
=================================

Typeclasses in talk
-------------------

Functor - lift unary f of part to unary f of whole
Lens - lift functor f of part to functor f of whole

Applicative - lift variadic f of part to variadic f of whole
Traversal - lift functor f of part to functor f of whole containing many parts

Background
----------

* Coarse-grained state.
* Fine-grained functions.
* Fine-grained functions are more composable.

Standard library
----------------

* get-in, assoc-in and update-in.

Functors
--------

* Functors lift functions of parts to functions of wholes.

Lenses
------

* Lenses are functions that lift functions of parts to functions of wholes.
* Get and set can be represented by a single function.
* Example - deeply nested map.
* Mapping structures onto each other
* Example - polar and cartesian coordinates.
* Arbitrary Functors.
* Example - maybe.
* Functional composition.
* Example - combination of nested map and imaginary numbers.

Pure Traversals
---------------

* The Functor side works.
* Example - sequences.
* Without return-type polymorphism, monoids don't work.

Traversy
--------

* Uses building blocks from the standard library.
* Composes easily - though not function composition.

Abstract concepts in untyped languages
--------------------------------------

* Rich claims that Transducers can't be typed.
* Rich uses types to sketch out how Transducers work.
