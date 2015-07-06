(ns journey-through-the-looking-glass.lens
  (:require
    [journey-through-the-looking-glass.maths :as maths]
    [journey-through-the-looking-glass.functor :as functor]
    [midje.sweet :refer :all])
  (:refer-clojure :exclude [get set]))

;;;;;;;;;;
; Lenses ;
;;;;;;;;;;


(defn seconds<>minutes
  "A lens that focuses on the minutes of an epoch of seconds."
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
      (update 120 seconds<>minutes maths/increment) => 180
      (set 120 seconds<>minutes 4) => 240
      (get 120 seconds<>minutes) => 2)


(defn safe-update
  "Update the focus of the lens, returning nil if it fails."
  [x lens f]
  (lens functor/fmaybe f x))

(fact "`safe-update` is like `update`, but nil-safe."
      (update 0 seconds<>minutes maths/reciprocal)
        => (throws NullPointerException)
      (safe-update 0 seconds<>minutes maths/reciprocal) => nil
      (safe-update 120 seconds<>minutes maths/reciprocal) => 30)


(defn compose
  "Compose two lenses to make a new one."
  [outer inner]
  (fn [functor f x]
    (outer functor #(inner functor f %) x)))

(def seconds<>hours
  "A lens that focuses on the hours of an epoch of seconds."
  (compose seconds<>minutes seconds<>minutes))

(fact "Lenses compose."
      (update 7200 seconds<>hours maths/increment) => 10800
      (set 7200 seconds<>hours 4) => 14400
      (get 7200 seconds<>hours) => 2)
