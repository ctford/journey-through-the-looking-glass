(ns journey-through-the-looking-glass.functor
  (:require
    [journey-through-the-looking-glass.maths :as maths]
    [midje.sweet :refer :all]))

; Functors
(defn fsequence
  [f x]
  (map f x))

(fact "The sequence functor applies a function to each element in a sequence."
      (fsequence maths/increment [1 2 3]) => [2 3 4])


(defn fminutes
  [f x]
  (-> x
      (* 1/60) ; convert seconds into minutes
      f        ; apply the function
      (* 60))) ; convert minutes back into seconds

(fact "The minutes functor applies a function to the minutes of an epoch."
      (fminutes maths/increment 1) => 61)


(defn combine [functor-1 functor-2]
  (fn [f x] (functor-1 (partial functor-2 f) x)))

(def fsequence-of-sequences (combine fsequence fsequence))

(fact "Functors compose."
      (fsequence-of-sequences maths/increment [[1 1] [2 2] [3 3]])
      => [[2 2] [3 3] [4 4]])


(defn fidentity
  [f x]
  (f x))

(fact "The identity functor applies the function normally."
      (fidentity inc 1) => 2)


(defn fconstant
  [f x]
  x)

(fact "The constant functor leaves the value untouched."
      (fconstant inc 1) => 1)
