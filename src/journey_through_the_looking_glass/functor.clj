(ns journey-through-the-looking-glass.functor
  (:require [midje.sweet :refer :all]))

; Functors
(defn fsequence [f] (partial map f))

(fact "The Sequence Functor applies a function to each element."
      (inc 1) => 2
      (inc [1 2 3]) => (throws ClassCastException)
      ((fsequence inc) [1 2 3]) => [2 3 4])


(defn fidentity [f] (identity f))

(fact "The Identity Functor applies a function to a value."
      ((fidentity inc) 3) => 4)


(defn fconstant [f] identity)

(fact "The Constant Functor ignores any applied function."
      ((fconstant inc) 1) => 1)


(defn fin [k f] (fn [x] (update-in x [k] f)))

(fact "The In Functor applies a function to a key's value."
      ((fin :x inc) {:x 1 :y 1}) => {:x 2 :y 1})


(fact "Functors compose like any other functions."
      (((comp fsequence (partial fin 0)) inc)
       [[1 1] [2 2] [3 3]])
      => [[2 1] [3 2] [4 3]]

      (((comp fsequence (partial fin :x) fsequence) inc)
       [{:x [1 2 3] :y 1} {:x [2 3 4] :y 1}])
      => [{:x [2 3 4] :y 1} {:x [3 4 5] :y 1}])


; Functor polymorphism
(defprotocol Functor
  (fmap [this f]))

(defrecord FunctorObject [functor value]
  Functor
  (fmap
    [this f]
    (-> this
        (get :value)                           ; Deconstruct
        ((functor f))                          ; Apply function
        ((partial ->FunctorObject functor))))) ; Reconstruct

(def ->Sequence (partial ->FunctorObject fsequence))
(def ->Identity (partial ->FunctorObject fidentity))
(def ->Constant (partial ->FunctorObject fconstant))

(fact "The Sequence Functor applies a function to each element."
      (fmap (->Sequence [1 2 3]) inc) => (->Sequence [2 3 4]))

(fact "The Identity Functor applies a function to a value."
      (fmap (->Identity 3) inc) => (->Identity 4))

(fact "The Constant Functor ignores any applied function."
      (fmap (->Constant 1) inc) => (->Constant 1))
