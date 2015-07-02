(ns journey-through-the-looking-glass.functor
  (:require [midje.sweet :refer :all]))

; Functors
(defn fsequence
  [f]
  (fn [x] (map f x)))

(fact "inc adds one to an integer"
      (inc 1) => 2)

(fact "inc doesn't work on sequences."
      (inc [1 2 3]) => (throws ClassCastException))

(fact "The Sequence Functor applies a function to each element in a sequence."
      ((fsequence inc) [1 2 3]) => [2 3 4])


(defn fminutes
  [f]
  (fn [x] (-> x (/ 60) f (* 60))) )

(fact "The Minutes Functor applies a function to the minutes of an epoch."
      ((fminutes inc) 1) => 61)


(fact "Functors compose like any other functions."
      (((comp fsequence fsequence) inc) [[1 1] [2 2] [3 3]])
          => [[2 2] [3 3] [4 4]])
