(ns journey-through-the-looking-glass.lens
  (:require 
    [journey-through-the-looking-glass.functor :as functor]
    [midje.sweet :refer :all]))

; Lenses
(defn in [k f]
  (fn [fmap]
    (fn [m]
      (let [map->value #(get % k)
            value->map #(assoc m k %)]
      (-> m
          map->value                       ; Deconstruct
          f                                ; Apply function
          ((fmap value->map)))))))         ; Apply reconstruction

(defn minutes [f]
  (fn [fmap]
    (fn [seconds]
      (let [seconds->minutes #(/ % 60)
            minutes->seconds #(* % 60)]
      (-> seconds
          seconds->minutes                 ; Deconstruct
          f                                ; Apply function
          ((fmap minutes->seconds)))))))   ; Apply reconstruction

(fact "Lenses can focus on any view of a structure."
      (((minutes inc) functor/fidentity) 60)
      => 120)


; Lens operations
(defn update [x lens f]
  (((lens f) functor/fidentity) x))

(defn put [x lens value]
  (update x lens (constantly value)))

(defn view [x lens]
  (((lens identity) functor/fconstant) x))

(defn compose [l1 l2]
  (fn [f]
    (fn [fmap]
      (let [inner-f ((l2 f) fmap)]
        ((l1 inner-f) fmap)))))


(fact "The In Lens supports the Lens operations."
      (-> {:x 1 :y 2} (update (partial in :x) inc)) => {:x 2 :y 2}
      (-> {:x 1 :y 2} (put (partial in :x) 99)) => {:x 99 :y 2}
      (-> {:x 1 :y 2} (view (partial in :x))) => 1)

(fact "The Minutes Lens supports the Lens operations."
      (-> 120 (update minutes dec)) => 60
      (-> 120 (put minutes 4)) => 240
      (-> 120 (view minutes)) => 2)


(fact "Lenses compose."
      (-> {:time 1}
          (update (compose (partial in :time) minutes) inc))
      => {:time 61})
