(ns journey-through-the-looking-glass.traversy
  (:require [midje.sweet :refer :all]))

(defn lens
  "Construct a lens from a focus :: x -> seq and an fmap :: f x -> x."
  [focus fmap]
  {:focus focus :fmap fmap})

(defn view
  "Return a seq of the lens' foci."
  [x lens]
  ((:focus lens) x))

(defn update
  "Apply f to the foci of x, as specified by lens."
  [x lens f]
  ((:fmap lens) f x))

(defn fapply [f x] (f x))
(def it
  (lens list fapply))

(fact "The 'it' lens is the identity."
  (-> 9 (view it)) => [9]
  (-> 9 (update it inc)) => 10)

(defn fconstant [f x] x)
(def nothing
  (lens (constantly []) fconstant))

(fact "The 'nothing' lens doesn't have a focus."
  (-> 9 (view nothing)) => []
  (-> 9 (update nothing inc)) => 9)

(def each
  (lens seq map))

(fact "The 'each' lens focuses on each item in a sequence."
  (-> [1 2 3] (view each)) => [1 2 3]
  (-> [1 2 3] (update each inc)) => [2 3 4]
  (-> [1 2 3] (update each inc)) => [2 3 4])


(defn fapply-in [path f x] (update-in x path f))

(defn in
  [path]
  (lens (fn [x] [(get-in x path)]) (partial fapply-in path)))

(fact "The 'in' lens focuses into a map based on a path."
  (-> {:foo 1} (view (in [:foo]))) => [1]
  (-> {:foo 1} (update (in [:foo]) inc)) => {:foo 2})


(defn fwhen [applicable? f x] (if (applicable? x) (f x) x))
(defn fsome [applicable? f x] (map (partial fwhen applicable? f) x))

(defn only
  [applicable?]
  (lens (partial filter applicable?) (partial fsome applicable?)))

(fact "The 'only' lens focuses on items in a sequence matching a condition."
  (-> [1 2 3] (view (only even?))) => [2]
  (-> [1 2 3] (update (only even?) inc)) => [1 3 3])


(defn combine
  [outer inner]
  (lens
    (fn [x] (mapcat #(view % inner) (view x outer)))
    (fn [f x] (update x outer #(update % inner f)))))

(fact "Lenses combine, but not with function composition."
  (-> {:foo [1 2]} (view (combine (in [:foo]) each))) => [1 2]
  (-> {:foo [1 2]} (update (combine (in [:foo]) each) inc)) => {:foo [2 3]})


(defn both
  [one another]
  (lens
    (fn [x] (concat (view x one) (view x another)))
    (fn [f x] (-> x (update one f) (update another f)))))

(fact "Lenses can be combined in parallel with 'both'."
  (-> {:foo 8 :bar 9}
      (view (both (in [:foo]) (in [:bar]))))
      => [8 9]

  (-> {:foo 8 :bar 9}
      (update (both (in [:foo]) (in [:bar])) inc))
      => {:foo 9 :bar 10})


(defn map-conj [f x]
  (->> x
       (map f)
       (reduce conj {})))

(def all-entries
  "A lens from map -> each entry."
  (lens seq map-conj))

(fact "The 'all-entries' lens focuses on the entries of a map."
  (-> {:foo 3 :bar 4}
      (view all-entries))
      => (just #{[:foo 3] [:bar 4]})

  (-> {:foo 3 :bar 4}
      (update all-entries (fn [[k v]] [v k])))
      => {3 :foo 4 :bar})


(def all-values
  (combine all-entries (in [1])))

(fact "The 'all-values' lens focuses on the values of a map."
  (-> {:foo 1 :bar 2} (view all-values)) => [1 2]
  (-> {:foo 1 :bar 2} (update all-values inc)) => {:foo 2 :bar 3})