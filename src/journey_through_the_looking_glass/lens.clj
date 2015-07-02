(ns journey-through-the-looking-glass.lens
  (:require
    [journey-through-the-looking-glass.maths :as maths]
    [midje.sweet :refer :all])
  (:refer-clojure :exclude [get set]))

; Lenses
(defn minutes
  [fmap f x]
  (-> x
      (/ 60)               ; convert seconds into minutes
      f                    ; apply the function
      ((fmap #(* % 60))))) ; convert minutes back into seconds


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

(defn compose
  [l1 l2]
  (fn [fmap f x]
    (l1 fmap (partial l2 fmap f) x)))


(fact "The Minutes Lens supports the Lens operations."
      (update 120 minutes maths/increment) => 180
      (set 120 minutes 4) => 240
      (get 120 minutes) => 2)


(def hours (compose minutes minutes))

(fact "Lenses compose."
      (update 7200 hours maths/increment) => 10800
      (set 7200 hours 4) => 14400
      (get 7200 hours) => 2)
