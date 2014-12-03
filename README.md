Journey through the looking glass
=================================

The problem
-----------

* I want to coordinate with coarse-grained state.
* I want to compose with fine-grained functions.
* These two ideals are in tension.

Clojure/core
------------

* `update-in` separates the focus from the applied function.
* The same paths work with `get-in` and `assoc-in`.
* However, `update-in` is specialised to one kind of focus.

Functors
--------

* "Functors are structure-preserving maps between Categories"
    - Barr and Wells.
* Functors are functions that lift functions into a context.
* Functors compose as functions.

Lenses
------

* Lenses are functions that lift contextualising functions into
  a context.
* `update`, `put`, `view` can all be represented by one function.
* Lenses compose as functions.

Traversals
----------

* Traversals are Lenses that can have more than one target.
* The `update` part works...
* ...but `view` needs variadic Functors (Applicatives) and
  Monoid targets.

Traversy
--------

* Uses building blocks from Clojure/core.
* Composes easily - though not through function composition.
* Doesn't have the full power of pure Lenses and Traversals.

References
----------

* [Category Theory for Computing Science](http://www.math.mcgill.ca/triples/Barr-Wells-ctcs.pdf) - Barr and Wells
* [Lenses: compositional data access and manipulation](https://skillsmatter.com/skillscasts/4251-lenses-compositional-data-access-and-manipulation) - Peyton Jones
* [The Essence of the Iterator Pattern](http://www.cs.ox.ac.uk/jeremy.gibbons/publications/iterator.pdf) - Gibbons
