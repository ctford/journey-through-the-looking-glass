(ns journey-through-the-looking-glass.lens)

; Typeclasses
(defprotocol Valuable
  (value [this]))

(defprotocol Functor
  (fmap [this f]))

; Instances
(defrecord Identity [x]
  Functor (fmap [this f] (-> x f ->Identity))
  Valuable (value [this] x))

(defrecord Constant [x]
  Functor (fmap [this _] this)
  Valuable (value [this] x))

; Functions
(defn update [x lens f]
  (-> x ((lens (comp ->Identity f))) value))

(defn put [x lens y]
  (update x lens (constantly y)))

(defn view [x lens]
  (-> x ((lens ->Constant)) value))

; Lenses
(defn minutes [f]
  (fn [x] (-> x (* 60) f (fmap (partial * 1/60)))))

(defn in [path]
  (fn [f]
    (fn [x]
      (-> x (get-in path) f (fmap (partial assoc-in x path))))))
