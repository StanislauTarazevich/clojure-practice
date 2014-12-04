(ns cmeans.core
  (:gen-class)
  (:use cmeans.file_reader)
  (:use cmeans.models)
  (:use cmeans.algorithm)
  (:use cmeans.distances))

(defn -main
  [& args]
  (find-clusters (get-collection-from-file "testdata/glass.txt"), euclid-distance))
