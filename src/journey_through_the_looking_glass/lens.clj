(ns journey-through-the-looking-glass.lens
  (:require 
    [journey-through-the-looking-glass.functor :as functor]
    [midje.sweet :refer :all]))

; Lenses
(defn in [k f]
  (fn [m]
    (-> m
        (get k)                            ; Deconstruct
        f                                  ; Apply function
        (functor/fmap #(assoc m k %)))))   ; Apply reconstruction

(defn adjacent
  [[x y]]
  (functor/->Sequence
    [[x (inc y)] [(inc x) y]
     [(dec x) y] [x (dec y)]]))

(fact "The In Lens focuses on a key's value."
      (-> {:position [1 1] :class "Wizard"}
          ((in :position adjacent)))
      => (functor/->Sequence
           [{:position [1 2], :class "Wizard"}
            {:position [2 1], :class "Wizard"}
            {:position [0 1], :class "Wizard"}
            {:position [1 0], :class "Wizard"}]))


(def apply-tax (partial * 1.10))

(defn number!?!?!
  [string]
  (functor/->Maybe
    (try
      (Integer/parseInt string)
      (catch Exception e))))

(fact "Using the Maybe Functor can return nil for the whole. "
      (-> {:price "90" :currency "AUD"}
          ((in :price number!?!?!))
          (functor/fmap :price)
          (functor/fmap apply-tax))
      => (functor/->Maybe 99.00000000000001)

      (-> {:price "foobar" :currency "AUD"}
          ((in :price number!?!?!))
          (functor/fmap :price)
          (functor/fmap apply-tax))
      => (functor/->Maybe nil))


(defn minutes [f]
  (fn [seconds]
    (-> seconds
        (/ 60)                             ; Deconstruct
        f                                  ; Apply function
        (functor/fmap (partial * 60)))))   ; Apply reconstruction

(fact "Lenses can focus on any view of a structure."
      ((minutes (comp functor/->Identity inc)) 1)
      => (functor/->Identity 61))


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
      (-> {:x 1 :y 2} (put (partial in :x) 99)) => {:x 99 :y 2}
      (-> {:x 1 :y 2} (view (partial in :x))) => 1)

(fact "The Minutes Lens supports the Lens operations."
      (-> 120 (update minutes dec)) => 60
      (-> 120 (put minutes 4)) => 240
      (-> 120 (view minutes)) => 2)
