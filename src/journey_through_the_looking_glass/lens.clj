(ns journey-through-the-looking-glass.lens
  (:require 
    [journey-through-the-looking-glass.functor :as functor]
    [midje.sweet :refer :all]))

; Lenses
(defn in [k f]
  (fn [m]
    (-> m
        (get k)                               ; Deconstruct
        f                                     ; Apply function
        (functor/fmap (partial assoc m k))))) ; Reconstruct

(defn up-to [n]
  (-> n
      range
      functor/->Sequence))

(fact "The In Lens focuses on a key's value."
  (-> {:x 3 :y 2}
      ((in :x up-to))
      (get :value))
      => [{:x 0 :y 2} {:x 1 :y 2} {:x 2 :y 2}])

(defn minutes [f]
  (fn [hours]
    (-> hours
        (* 60)                                ; Deconstruct
        f                                     ; Apply function
        (functor/fmap (partial * 1/60)))))    ; Reconstruct

; Lens operations
(defn update [x lens f]
  (-> x
      ((lens (comp functor/->Identity f)))
      (get :value)))

(defn put [x lens value]
  (update x lens (constantly value)))

(defn view [x lens]
  (-> x
      ((lens functor/->Constant))
      (get :value)))


(fact "The In Lens supports the Lens operations."
  (-> {:x 1 :y 2} (update (partial in :x) inc)) => {:x 2 :y 2}
  (-> {:x 1 :y 2} (view (partial in :x))) => 1)

(fact "The Minutes Lens supports the Lens operations."
  (-> 3 (update minutes (partial + 60))) => 4
  (-> 3 (view minutes)) => 180)
