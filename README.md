Journey through the looking glass
=================================

The problem
-----------

* I want to coordinate with coarse-grained state.
* I want to compose with fine-grained functions.
* These two ideals are in tension.

Functors
--------

* "Functors are structure-preserving maps between Categories" - Barr and Wells.
* Functors represent different strategies for applying functions.
* Functors compose.

Lenses
------

* Lenses deconstruct values and put them back together again, letting a functor
  control the reconstruction. 
* `update`, `set`, `get` (and more) can all be represented by one function.
* Lenses compose.

Traversals
----------

* Traversals are Lenses that can have more than one target.
* The `update` part works...
* ...but `get` needs variadic Functors (Applicatives) and Monoid targets to sum up the results.
* Traversals compose.

Traversy
--------

* Uses building blocks from Clojure's standard library.
* Has a rich set of lenses for Clojure's standard datatypes.
* Lacks the full power/complexity of pure Lenses and Traversals.
* Traversy's lenses compose.

References
----------

* [Category Theory for Computing Science](http://www.math.mcgill.ca/triples/Barr-Wells-ctcs.pdf) - Barr and Wells
* [Lenses: Compositional Data Access and Manipulation](https://skillsmatter.com/skillscasts/4251-lenses-compositional-data-access-and-manipulation) - Peyton Jones
* [The Essence of the Iterator Pattern](http://www.cs.ox.ac.uk/jeremy.gibbons/publications/iterator.pdf) - Gibbons
* [Through the Looking Glass](http://www.gutenberg.org/ebooks/12) - Carrol
* [Traversy](https://github.com/ctford/traversy) - Ford

Unit tests
----------

* Run `lein midje`.
