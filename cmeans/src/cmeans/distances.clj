(ns cmeans.distances
  (import java.lang.Math))

(defn euclid-distance [a, b]
	(Math/sqrt (reduce + (map (comp #(Math/pow %1 2) -) a, b))))

(euclid-distance '(2 3) '(3 4))

(defn hamming-distance [a, b]
	(reduce + (map (fn [e, v] (if (= e v) 0 1)) a, b)))

(hamming-distance '(1 2 3 4) '(1 2 5 4))
