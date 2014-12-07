(ns webcrawler.models)

(defrecord Webpage [url status body depth urls children redirect-info])
