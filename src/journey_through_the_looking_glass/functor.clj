(ns journey-through-the-looking-glass.functor
  (:require
    [journey-through-the-looking-glass.maths :as maths]
    [midje.sweet :refer :all]))

; Functors
(defn fsequence
  "The sequence functor."
  [f x]
  (map f x))

(fact "The sequence functor applies a function to each element in a sequence."
      (fsequence maths/increment [1 2 3]) => [2 3 4])


(defn fminutes
  "The minutes functor."
  [f x]
  (-> x
      (* 1/60) ; convert seconds into minutes
      f        ; apply the function
      (* 60))) ; convert minutes back into seconds

(fact "The minutes functor applies a function to the minutes of an epoch."
      (fminutes maths/increment 1) => 61)


(defn combine
  "Combine two functors to make a new one."
  [outer inner]
  (fn [f x] (outer (partial inner f) x)))

(def fsequence-of-sequences
  "The sequence-of-sequences functor."
  (combine fsequence fsequence))

(fact "Functors compose."
      (fsequence-of-sequences maths/increment [[1 1] [2 2] [3 3]])
      => [[2 2] [3 3] [4 4]])


(defn fidentity
  "The identity functor."
  [f x]
  (f x))

(fact "The identity functor applies the function normally."
      (fidentity maths/increment 1) => 2)


(defn fconstant
  "The constant functor."
  [f x]
  x)

(fact "The constant functor leaves the value untouched."
      (fconstant maths/increment 1) => 1)
