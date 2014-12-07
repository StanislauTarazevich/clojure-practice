(ns webcrawler.core
  (:gen-class)
  (:use webcrawler.crawler)
  (:use webcrawler.file_reader)
  (:use webcrawler.output))

(defn get-urls-to-start
	[file-path]
	(retrieve-lines file-path))

(defn -main
  [& args]
  (let [path (first args)
        depth (read-string (last args))
        start-urls (get-urls-to-start path)
        root (create-root-node start-urls depth)
        crawled-root (crawl-node nil root (:depth root))]
    (shutdown-agents)
    (print-nodes crawled-root 0)))
