(ns journey-through-the-looking-glass.test.lens
  (:require [midje.sweet :refer :all]
            [journey-through-the-looking-glass.lens :refer :all]))

(fact "The Identity Functor applies a function to a value."
      ((fidentity inc) 3) => 4)

(fact "The Constant Functor ignores any applied function."
      ((fconstant inc) 3) => 3)

(fact "The Maybe Functor only applies a function when a value is present."
      ((fmaybe inc) 3) => 4
      ((fmaybe inc) nil) => nil)

(fact "The Sequence Functor applies a function to each element in the sequence."
      ((fsequence inc) [1 2 3]) => [2 3 4])

(fact "Functors compose."
      (((comp fsequence fmaybe) inc) [1 nil 3]) => [2 nil 4])

(fact "The in Functor applies a function to the value corresponding to a key within a map."
      ((fin :x inc) {:x 1 :y 1}) => {:x 2 :y 1})

(fact "The minutes Lens supports the Lens operations."
  (-> 3 (update minutes (partial + 60))) => 4
  (-> 3 (view minutes)) => 180)

(fact "The in Lens supports the Lens operations."
  (-> {:x 1 :y 2} (update (partial in :x) inc)) => {:x 2 :y 2}
  (-> {:x 1 :y 2} (view (partial in :x))) => 1)
