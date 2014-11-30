Journey through the looking glass
=================================

Background
----------

* Coordination of coarse-grained state is simpler.
* Composition of fine-grained functions is simpler.
* These two goals are in tension.

Standard library
----------------

* `update-in` lets us separate focusing on a substructure from the function we apply.
* The same paths work with `get-in` and `assoc-in`.
* However, `update-in` is specialised to a particular kind of focus.

Functors
--------

* "Functors are structure-preserving maps between Categories" - Barr and Wells.
* Functors are functions that lift functions into a context.
* Functors compose as functions.

Lenses
------

* Lenses are functions that lift functions into a context.
* `update`, `put`, `view` and more can all be represented by a single function.
* Lenses compose as functions.

Traversals
----------

* Traversals are Lenses that can have more than one target.
* The `update` and `put` parts work...
* ...but `view` requires variadic Functors (Applicatives) and Monoid targets. 

Traversy
--------

* Uses building blocks from the standard library.
* Composes easily - though not through function composition.
* Doesn't have the full power of pure Lenses and Traversals.
