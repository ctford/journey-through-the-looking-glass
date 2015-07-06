(ns journey-through-the-looking-glass.lens
  (:require
    [journey-through-the-looking-glass.maths :as maths]
    [journey-through-the-looking-glass.functor :as functor]
    [midje.sweet :refer :all])
  (:refer-clojure :exclude [get set]))

; Lenses
(defn minutes
  "A lens that focuses on the minutes of an epoch."
  [functor f x]
    (->> x
      (* 1/60)              ; convert seconds into minutes
      f                     ; apply the function
      (functor #(* % 60)))) ; convert minutes back into seconds...
                            ; ...using a functor!


; Lens operations
(defn update
  "Update the focus of the lens with the function f."
  [x lens f]
  (lens functor/fidentity f x))

(defn set
  "Set the value at the focus of the lens."
  [x lens value]
  (update x lens (constantly value)))

(defn get
  "Get the value at the focus of the lens."
  [x lens]
  (lens functor/fconstant identity x))

(fact "The minutes lens supports the lens operations."
      (update 120 minutes maths/increment) => 180
      (set 120 minutes 4) => 240
      (get 120 minutes) => 2)


(defn try-and-update
  "Update the focus of the lens, tolerating the result being nil."
  [x lens f]
  (lens functor/fmaybe f x))

(fact "try-and-update is like update, but nil-safe."
      (update 0 minutes maths/reciprocal) => (throws NullPointerException)
      (try-and-update 0 minutes maths/reciprocal) => nil
      (try-and-update 120 minutes maths/reciprocal) => 30)


(defn compose
  "Compose two lenses to make a new one."
  [outer inner]
  (fn [functor f x]
    (outer functor #(inner functor f %) x)))

(def hours
  "A lens that focuses on the hours of an epoch."
  (compose minutes minutes))

(fact "Lenses compose."
      (update 7200 hours maths/increment) => 10800
      (set 7200 hours 4) => 14400
      (get 7200 hours) => 2)
