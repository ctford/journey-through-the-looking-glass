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
