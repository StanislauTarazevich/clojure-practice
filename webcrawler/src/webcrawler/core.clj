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
  (let [url (first args)
        depth (read-string (last args))
        start-urls (get-urls-to-start url)
        root (create-root-node start-urls depth)]
    (print-nodes (crawl-node root (:depth root)) 0)))
