(ns journey-through-the-looking-glass.lens)

; Functors
(defn fsequence [f] (partial map f))
(defn fidentity [f] (identity f))
(defn fmaybe [f] (fn [x] (some-> x f)))
(defn fpath [k f] (fn [x] (update-in x [k] f)))
(defn fconstant [f] identity)

(defn inject [t functor]
  (fn [f]
    (fn [x]
      ((functor (comp f t)) x))))

; Lens operations
(defn update [x lens f]
  ((lens (inject f fidentity)) x))

(defn put [x lens value]
  (update x lens (constantly value)))

(defn view [x lens]
  ((lens fconstant) x))

; Lenses
(defn minutes [f]
  (fn [x]
    ((f #(/ % 60)) (* x 60))))

(defn in [path]
  (fn [f]
    (fn [x]
      ((f (partial assoc-in x path)) (get-in x path)))))
