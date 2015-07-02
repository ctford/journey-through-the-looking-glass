(ns journey-through-the-looking-glass.functor
  (:require
    [journey-through-the-looking-glass.maths :as maths]
    [midje.sweet :refer :all]))

; Functors
(defn fsequence
  [f]
  (fn [x] (map f x)))

(fact "The sequence functor applies a function to each element in a sequence."
      ((fsequence maths/increment) [1 2 3]) => [2 3 4])


(defn fminutes
  [f]
  (fn [x]
    (-> x
        (/ 60)    ; convert seconds into minutes
        f         ; apply the function
        (* 60)))) ; convert minutes back into seconds

(fact "The minutes functor applies a function to the minutes of an epoch."
      ((fminutes maths/increment) 1) => 61)


(def fsequence-of-sequences (comp fsequence fsequence))

(fact "Functors compose like any other functions."
      ((fsequence-of-sequences maths/increment) [[1 1] [2 2] [3 3]])
          => [[2 2] [3 3] [4 4]])
