(ns webcrawler.file_reader
	(:require [clojure.java.io :as io])
  (:require [clojure.string  :as str]))

(defn- to-list [line, delimiter]
    (map read-string (drop-last(str/split line delimiter))))

(defn retrieve-data [path, delimiter]
  	(with-open [reader (io/reader path)]
    	(let [lines (line-seq reader)]
            (doall (map #(to-list %, delimiter) lines)))))

(defn retrieve-lines [path]
  	(with-open [reader (io/reader path)]
    	(doall (line-seq reader))))
