(ns cmeans.algorithm
  (:gen-class)
  (:use cmeans.distances)
  (:use cmeans.models)
  (:use cmeans.file_reader)
	(import java.lang.Math))

(def ra 3)
(def rb (* 1.5 ra))
(def alpha (/ 4 (* ra ra)))
(def beta (/ 4 (* rb rb)))
(def eps-l 0.15)
(def eps-h 0.5)

(get-collection-from-file "testdata/bezdekIris.data")

(defn- calc-distance
  [a, b, distance-fn]
  (distance-fn (:coords a) (:coords b)))

(defn- calc-init-potential
  [point, collection, distance-fn]
  (reduce + (map (fn [elem] (Math/exp (- (* (calc-distance elem point distance-fn) alpha)))) collection)))

(defn- calc-init-potentials
  [collection, distance-fn]
  (map (fn [elem] (struct-map point :index (:index elem) :coords (:coords elem) :potential (calc-init-potential elem collection distance-fn))) collection))

(defn- calc-next-potential
  [point, curr-center, distance-fn]
  (- (:potential point) (* (:potential curr-center) (Math/exp (- (* (calc-distance point curr-center distance-fn) beta))))))

(defn- calc-next-potentials
  [collection, curr-center, distance-fn]
  (map (fn [elem] (struct-map point :index (:index elem) :coords (:coords elem) :potential (calc-next-potential elem curr-center distance-fn))) collection))

(defn- get-max-potential-point
  [collection]
  (apply max-key (fn [x] (:potential x)) collection))

(defn- min-distance
  [point centers distance-fn]
  (apply min (map #(calc-distance point %1 distance-fn) centers)))

(defn- potential-to-zero
  [points index]
  (map #(if (= (:index %1) index) (assoc %1 :potential 0) %1) points))

(defn- add-center
  [collection centers first-center new-center distance-fn]
  (cond
     (> (:potential new-center) (* (:potential first-center) eps-h))
     (struct-map app-state :centers (cons new-center centers) :points collection :has-new true)
     (< (:potential new-center) (* (:potential first-center) eps-l))
     (struct-map app-state :centers centers :points collection :has-new false)
     (>= (+ (/ (min-distance new-center centers distance-fn) ra) (/ (:potential new-center) (:potential first-center))))
     (struct-map app-state :centers (cons new-center centers) :points collection :has-new true)
     :else (struct-map app-state :centers centers :points (potential-to-zero collection (:index new-center)) :has-new true)))

(defn- get-next-max-potential
  [centers collection first-center distance-fn]
  (let [new-collection (calc-next-potentials collection (first centers) distance-fn)
        new-center (get-max-potential-point new-collection)]
    (add-center new-collection centers first-center new-center distance-fn)))

(defn find-clusters
  [points distance-fn]
  (let [init-points (calc-init-potentials points distance-fn)
        first-center (get-max-potential-point init-points)]
    (loop [centers [first-center] collection init-points]
      (let [new-app-state (get-next-max-potential centers collection first-center distance-fn)]
        (if (:has-new new-app-state)
            (recur (:centers new-app-state) (:points new-app-state))
            (:centers new-app-state))))))
