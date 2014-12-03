(ns journey-through-the-looking-glass.functor
  (:require [midje.sweet :refer :all]))

; Functors
(defn fsequence [f] (partial map f))

(fact "The Sequence Functor applies a function to each element in the sequence."
      (inc 1) => 2
      (inc [1 2 3]) => (throws ClassCastException)
      ((fsequence inc) [1 2 3]) => [2 3 4])


(defn fmaybe [f] (fn [x] (some-> x f)))

(fact "The Maybe Functor only applies a function when a value is present."
      (inc 3) => 4 
      ((fmaybe inc) 3) => 4
      (inc nil) => (throws NullPointerException)
      ((fmaybe inc) nil) => nil)


(defn fidentity [f] (identity f))

(fact "The Identity Functor applies a function to a value."
      ((fidentity inc) 3) => 4)


(defn fconstant [f] identity)

(fact "The Constant Functor ignores any applied function."
      ((fconstant inc) 1) => 1)


(defn fin [k f] (fn [x] (update-in x [k] f)))

(fact "The in Functor applies a function to a key's value within a map."
      ((fin :x inc) {:x 1 :y 1}) => {:x 2 :y 1})


(fact "Functors compose like any other functions."
      (((comp fsequence fmaybe) inc) [1 nil 3]) => [2 nil 4]
      (((comp fsequence (partial fin :x) fmaybe) inc)
       [{:x 1 :y 1} {:x nil :y 1}])
      => [{:x 2 :y 1} {:x nil :y 1}])


; Functor polymorphism
(defprotocol Functor
  (fmap [this f]))

(defrecord Sequence [value]
  Functor
  (fmap [this f] (-> this
                     (get :value)    ; Deconstruct
                     ((fsequence f)) ; Apply function
                     ->Sequence)))   ; Reconstruct

(fact "The Sequence Functor applies a function to each element in the sequence."
      (fmap (->Sequence [1 2 3]) inc) => (->Sequence [2 3 4]))


(defrecord Maybe [value]
  Functor
  (fmap [this f] (-> this
                     (get :value)    ; Deconstruct
                     ((fmaybe f))    ; Apply function
                     ->Maybe)))      ; Reconstruct

(fact "The Maybe Functor only applies a function when a value is present."
      (fmap (->Maybe 3) inc) => (->Maybe 4)
      (fmap (->Maybe nil) inc) => (->Maybe nil))


(defrecord Identity [value]
  Functor
  (fmap [this f] (-> this
                     (get :value)    ; Deconstruct
                     ((fidentity f)) ; Apply function
                     ->Identity)))   ; Reconstruct

(fact "The Identity Functor applies a function to a value."
      (fmap (->Identity 3) inc) => (->Identity 4))


(defrecord Constant [value]
  Functor
  (fmap [this f] (-> this
                     (get :value)    ; Deconstruct
                     ((fconstant f)) ; Apply function
                     ->Constant)))   ; Reconstruct

(fact "The Constant Functor ignores any applied function."
      (fmap (->Constant 1) inc) => (->Constant 1))
