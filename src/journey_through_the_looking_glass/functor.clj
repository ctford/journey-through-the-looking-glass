(ns journey-through-the-looking-glass.functor
  (:require [midje.sweet :refer :all]))

; Functors
(defn fsequence
  [f]
  (fn [x] (map f x)))

(fact "The Sequence Functor applies a function to each element."
      (inc 1) => 2
      (inc [1 2 3]) => (throws ClassCastException)
      ((fsequence inc) [1 2 3]) => [2 3 4])


(defn fin
  [k f]
  (fn [x] (update-in x [k] f)))

(fact "The In Functor applies a function to a key's value."
      ((fin :x inc) {:x 1 :y 1}) => {:x 2 :y 1})


(fact "Functors compose like any other functions."
      (((comp fsequence (partial fin 0)) inc)
       [[1 1] [2 2] [3 3]])
      => [[2 1] [3 2] [4 3]]

      (((comp fsequence (partial fin :x) fsequence) inc)
       [{:x [1 2 3] :y 1} {:x [2 3 4] :y 1}])
      => [{:x [2 3 4] :y 1} {:x [3 4 5] :y 1}])
