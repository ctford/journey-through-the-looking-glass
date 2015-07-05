(ns journey-through-the-looking-glass.maths
  (:require [midje.sweet :refer :all]))


(fact "The function always goes at the front."
      (+ 1 1) => 2)


(defn increment
  "A Clojure equivalent of '++'."
  [x]
  (+ 1 x))

(fact "increment adds one to an integer."
      (increment 1) => 2)

(fact "increment doesn't work on sequences."
      (increment [1 2 3]) => (throws ClassCastException))


(defn reciprocal
  "Take the reciprocal of a fraction."
  [x]
  (if-not (zero? x)
    (/ 1 x)
    nil))

(fact "reciprocal flips fractions"
      (reciprocal 0) => nil
      (reciprocal 1) => 1
      (reciprocal 1/2) => 2
      (reciprocal 3) => 1/3)

(fact "reciprocal doesn't work on sequences either."
      (reciprocal [1 2 3]) => (throws ClassCastException))
