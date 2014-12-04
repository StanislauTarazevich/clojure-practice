(ns cmeans.core
  (:gen-class)
  (:use cmeans.file_reader)
  (:use cmeans.models)
  (:use cmeans.algorithm)
  (:use cmeans.distances))

(defn- determine-distance
	[function-name]
	(case function-name
		"hamming" hamming-distance,
		"euclid" euclid-distance))

(defn -main
  [& args]
  (let [path (first args)
        distance-fn (determine-distance (last args))]
  (doseq [center (find-clusters (get-collection-from-file path), distance-fn)]
    (println center))))
