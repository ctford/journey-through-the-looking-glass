(ns journey-through-the-looking-glass.maths
  (:require [midje.sweet :refer :all]))


(defn increment
  [x]
  (+ 1 x))

(fact "increment adds one to an integer."
      (increment 1) => 2)

(fact "increment doesn't work on sequences."
      (increment [1 2 3]) => (throws ClassCastException))


(defn double-increment
  [x]
  ((comp increment increment) x))

(fact "double-increment adds two to an integer."
      (double-increment 1) => 3)

(fact "double-increment doesn't work on sequences either."
      (double-increment [1 2 3]) => (throws ClassCastException))
