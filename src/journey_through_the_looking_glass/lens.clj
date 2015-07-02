(ns journey-through-the-looking-glass.lens
  (:require
    [journey-through-the-looking-glass.maths :as maths]
    [midje.sweet :refer :all])
  (:refer-clojure :exclude [get set]))

; Lenses
(defn minutes
  [functor f x]
  (-> x
      (/ 60)                  ; convert seconds into minutes
      f                       ; apply the function
      ((functor #(* % 60))))) ; convert minutes back into seconds


; Lens operations
(defn update
  [x lens f]
  (lens identity f x))

(defn set
  [x lens value]
  (update x lens (constantly value)))

(defn get
  [x lens]
  (lens (constantly identity) identity x))

(fact "The minutes lens supports the lens operations."
      (update 120 minutes maths/increment) => 180
      (set 120 minutes 4) => 240
      (get 120 minutes) => 2)


(defn compose
  [outer inner]
  (fn [functor f x]
    (outer functor (partial inner functor f) x)))

(def hours (compose minutes minutes))

(fact "Lenses compose."
      (update 7200 hours maths/increment) => 10800
      (set 7200 hours 4) => 14400
      (get 7200 hours) => 2)
