(ns webcrawler.core
  (:gen-class)
  (:use webcrawler.crawler)
  (:use webcrawler.file_reader)
  (:use webcrawler.output))

(def depth 2)

(defn get-urls-to-start
	[file-path]
	(retrieve-lines file-path))

(get-urls-to-start "urls.txt")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [start-urls (get-urls-to-start "urls.txt")
        root (create-root-node start-urls depth)]
    (print-nodes (crawl-node root (:depth root)) 0)))
