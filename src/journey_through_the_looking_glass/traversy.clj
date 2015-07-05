(ns journey-through-the-looking-glass.traversy
  (:require [traversy.lens :refer [update view in each conditionally *> all-entries]]
            [midje.sweet :refer :all]))


(fact "The 'each' lens focuses on each item in a sequence."
  (-> [1 2 3] (view each)) => [1 2 3]
  (-> [1 2 3] (update each inc)) => [2 3 4])

(fact "The 'in' lens focuses into a map based on a path."
  (-> {:foo 1} (view (in [:foo]))) => [1]
  (-> {:foo 1} (update (in [:foo]) inc)) => {:foo 2})

(fact "Lenses combine, but not with function composition."
  (-> {:foo [1 2 3]}
      (view (*> (in [:foo]) each)))
      => [1 2 3]

  (-> {:foo [1 2 3]}
      (update (*> (in [:foo]) each) inc))
      => {:foo [2 3 4]})


(defn only [applicable?] (*> each (conditionally applicable?)))

(fact "The 'only' lens focuses on some items in a sequence."
  (-> [1 2 3] (view (only even?))) => [2]
  (-> [1 2 3] (update (only even?) inc)) => [1 3 3])


(def value (in [1]))

(def all-values (*> all-entries value))

(fact "The 'all-values' lens focuses on the values of a map."
  (-> {:foo 1 :bar 2} (view all-values)) => [1 2]
  (-> {:foo 1 :bar 2} (update all-values inc)) => {:foo 2 :bar 3})
