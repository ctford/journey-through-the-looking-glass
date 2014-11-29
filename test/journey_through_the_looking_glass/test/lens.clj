(ns journey-through-the-looking-glass.test.lens
  (:require [midje.sweet :refer :all]
            [journey-through-the-looking-glass.lens :refer :all]))

(fact "The minutes lens supports the three lens operations."
  (-> 3 (update minutes (partial + 30))) => 7/2
  (-> 3 (view minutes)) => 180
  (-> 3 (put minutes 120)) => 2)

(fact "The in lens supports the three lens operations."
  (-> {:x 1 :y 2} (update (in [:x]) inc)) => {:x 2 :y 2}
  (-> {:x 1 :y 2} (view (in [:x]))) => 1
  (-> {:x 1 :y 2} (put (in [:x]) 7)) => {:x 7 :y 2})

(fact "Lenses compose with ordinary function composition."
  (-> {:hours 1} (update (comp (in [:hours]) minutes) inc)) => {:hours 61/60}
  (-> {:hours 1} (view (comp (in [:hours]) minutes))) => 60
  (-> {:hours 1} (put (comp (in [:hours]) minutes) 120)) =>  {:hours 2})

(fact "The each traversal iterates through a sequence."
   (-> [[1] [2] [3]] (update each (partial map inc))) => [[2] [3] [4]]
   (-> [[1] [2] [3]] (view each)) => [1 2 3])
