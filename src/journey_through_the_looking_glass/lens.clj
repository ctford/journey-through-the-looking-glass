(ns journey-through-the-looking-glass.lens)

; Typeclasses
(defprotocol Functor
  (fmap [this f]))

(defn fsequence [f] (partial map f))

(defn fidentity [f] f)

(defn fmaybe [f] #(some-> % f))

(defn fconstant [f] identity)

(defn fpath [k f] #(update-in % [k] f))

; Instances
(defrecord Identity [value]
  Functor (fmap [this f] (-> value f ->Identity)))

(defrecord Constant [value]
  Functor (fmap [this _] this))

(defrecord Maybe [value]
  Functor (fmap [this f] (some-> value f ->Maybe)))

(defrecord Sequence [value]
  Functor (fmap [this f] (->> value (map f) ->Sequence)))

; Functions
(defn update [x lens f]
  (-> x ((lens (comp ->Identity f))) :value))

(defn put [x lens y]
  (update x lens (constantly y)))

(defn view [x lens]
  (-> x ((lens ->Constant)) :value))

; Lenses
(defn minutes [f]
  (fn [x] (-> x (* 60) f (fmap (partial * 1/60)))))

(defn in [path]
  (fn [f]
    (fn [x]
      (-> x (get-in path) f (fmap (partial assoc-in x path))))))

; Traversals
(defprotocol Applicative
  (fapply [this f args]))

(extend-protocol Applicative
  Identity
  (fapply [this f args] (->Identity (apply f (:value this) [(:value args)])))

  Constant
  (fapply [this _ args] (->Constant (apply concat (:value this) [(:value args)]))))

(defn raise [x] (map (fn [y] [y]) x))
(defn lower [x] (apply concat x))

(defn updates [x lens f]
  (-> x raise (update lens (partial map f)) lower))

(defn each [f]
  (fn [xs]
    (reduce
      (fn [accumulator x] (fapply (f x) cons accumulator))
      []
      (reverse xs))))
