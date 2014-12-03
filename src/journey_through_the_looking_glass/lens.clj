(ns journey-through-the-looking-glass.lens
  (:require 
    [journey-through-the-looking-glass.functor :as functor]
    [midje.sweet :refer :all]))

; Lenses
(defn in [k f]
  (fn [m]
    (-> m
        (get k)                          ; Deconstruct
        f                                ; Apply function
        (functor/fmap #(assoc % m k))))) ; Apply reconstruction

(def up-to (comp functor/->Sequence range))

(fact "The In Lens focuses on a key's value."
      ((in :x up-to) {:x 3 :y 2})
      => (functor/->Sequence [{:x 0 :y 2} {:x 1 :y 2} {:x 2 :y 2}]))


(defn exclaim [string]
  (functor/fmap (functor/->Maybe string) #(str % "!!!!!")))

(fact "Using the Maybe Functor can return nil for the whole. "
      ((in :last-name exclaim)
       {:first-name "Bruce" :last-name "Durling"})
      => (functor/->Maybe
           {:first-name "Bruce" :last-name "Durling!!!!!"})

      ((in :last-name exclaim)
       {:first-name "Aphyr"})
      => (functor/->Maybe
           nil))


(defn minutes [f]
  (fn [hours]
    (-> hours
        (* 60)                           ; Deconstruct
        f                                ; Apply function
        (functor/fmap #(/ % 60)))))      ; Apply reconstruction

(fact "Lenses can focus on any view of a structure."
      ((minutes (comp functor/->Identity (partial + 60))) 3)
      => (functor/->Identity 4))


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
  (-> 3 (update minutes (partial + 60))) => 4
  (-> 3 (put minutes 60)) => 1
  (-> 3 (view minutes)) => 180)
