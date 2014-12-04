(ns cmeans.file_reader
  (:use cmeans.models)
	(:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn- to-list [line, delimiter]
    (map read-string (drop-last(str/split line delimiter))))

(defn- retrieve-data [path, delimiter]
  	(with-open [reader (io/reader path)]
    	(let [lines (line-seq reader)]
            (doall (map #(to-list %, delimiter) lines)))))

(defn get-collection-from-file
  [file-path]
  (map (fn [a,b] (struct-map point :index a :coords b :curr-potential 0)) (range) (retrieve-data file-path #",")))
