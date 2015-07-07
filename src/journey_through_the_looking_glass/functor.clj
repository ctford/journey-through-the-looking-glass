(ns journey-through-the-looking-glass.functor
  (:require
    [journey-through-the-looking-glass.maths :as maths]
    [midje.sweet :refer :all]))

;;;;;;;;;;;;
; Functors ;
;;;;;;;;;;;;


(defn fsequence
  "The sequence functor."
  [f sequence-of-things]
  (map f sequence-of-things))

(fact "The sequence functor applies a function across a sequence."
      (fsequence maths/increment [1 2 3]) => [2 3 4])


(defn fminutes
  "The minutes functor."
  [f seconds]
  (-> seconds
      (* 1/60) ; convert seconds into minutes
      f        ; apply the function
      (* 60))) ; convert minutes back into seconds

(fact "The minutes functor applies a function to the minutes of an epoch."
      (fminutes maths/increment 1) => 61)


(defn fmaybe
  "The maybe functor."
  [f optional-value]
  (if-not (nil? optional-value)
    (f optional-value)
    nil))

(fact "The maybe functor only applies a function when the value isn't nil."
      (maths/increment nil) => (throws NullPointerException)
      (fmaybe maths/increment nil) => nil
      (fmaybe maths/increment 1) => 2)


(defn combine
  "Combine two functors to make a new one."
  [outer inner]
  (fn [f x] (outer #(inner f %) x)))

(def fsafe-sequence
  "The safe-sequence functor."
  (combine fsequence fmaybe))

(fact "Functors compose."
      (fsequence maths/increment [1 nil 3]) => (throws NullPointerException)
      (fsafe-sequence maths/increment [1 nil 3]) => [2 nil 4])


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
