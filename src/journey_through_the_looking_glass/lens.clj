(ns journey-through-the-looking-glass.lens
  (:require
    [journey-through-the-looking-glass.maths :as maths]
    [journey-through-the-looking-glass.functor :as functor]
    [midje.sweet :refer :all])
  (:refer-clojure :exclude [get set]))

; Lenses
(defn minutes
  [f]
  (fn [fmap]
    (fn [seconds]
      (let [seconds->minutes #(/ % 60)
            minutes->seconds #(* % 60)]
        (-> seconds
            seconds->minutes                 ; Deconstruct
            f                                ; Apply function
            ((fmap minutes->seconds)))))))   ; Apply reconstruction

(fact "Lenses can focus on any view of a structure."
      (((minutes maths/increment) functor/fidentity) 60)
      => 120)


; Lens operations
(defn update
  [x lens f]
  (((lens f) (fn [f] (fn [x] (f x)))) x))

(defn set
  [x lens value]
  (update x lens (constantly value)))

(defn get
  [x lens]
  (((lens identity) (constantly identity)) x))

(defn compose
  [l1 l2]
  (fn [f]
    (fn [fmap]
      ((l1 ((l2 f) fmap)) fmap))))


(fact "The Minutes Lens supports the Lens operations."
      (-> 120 (update minutes dec)) => 60
      (-> 120 (set minutes 4)) => 240
      (-> 120 (get minutes)) => 2)


(def hours (compose minutes minutes))

(fact "Lenses compose."
      (-> 7200 (update hours maths/increment)) => 10800
      (-> 7200 (set hours 4)) => 14400
      (-> 7200 (get hours)) => 2)
