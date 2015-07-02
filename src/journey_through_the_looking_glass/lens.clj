(ns journey-through-the-looking-glass.lens
  (:require
    [journey-through-the-looking-glass.maths :as maths]
    [midje.sweet :refer :all])
  (:refer-clojure :exclude [get set]))

; Lenses
(defn minutes
  [fmap f x]
    (let [seconds->minutes #(/ % 60)
          minutes->seconds #(* % 60)]
      (-> x
          seconds->minutes                 ; Deconstruct
          f                                ; Apply function
          ((fmap minutes->seconds)))))     ; Apply reconstruction


; Lens operations
(defn update
  [x lens f]
  (lens (fn [f] (fn [x] (f x))) f x))

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
      (-> 120 (update minutes maths/increment)) => 180
      (-> 120 (set minutes 4)) => 240
      (-> 120 (get minutes)) => 2)


(def hours (compose minutes minutes))

(fact "Lenses compose."
      (-> 7200 (update hours maths/increment)) => 10800
      (-> 7200 (set hours 4)) => 14400
      (-> 7200 (get hours)) => 2)
