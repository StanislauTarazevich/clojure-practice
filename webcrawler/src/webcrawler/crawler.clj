(ns webcrawler.crawler
  (:require [clj-http.client :as client])
  (:require [net.cgrand.enlive-html :as html])
  (:require [webcrawler.models])
  (:import [webcrawler.models Webpage]))

(def not-nil? (complement nil?))

(def http-client-options
    { :max-redirects 1
	  :socket-timeout 5000
	  :conn-timeout 5000 } )

(defn remove-nils
	[hrefs]
	(filter #(not (nil? %)), hrefs))

(defn exclude-self-refs
	[hrefs, url]
	(filter #(boolean (not (.contains %1 url))) hrefs))

(defn convert-to-base
	[hrefs]
	(distinct (remove-nils (map #(re-find #"http.*\.[a-zA-Z]+/", %), hrefs))))

(defn get-hrefs
	[body url]
    (let [snippets (html/html-snippet body)
          hrefs (convert-to-base (exclude-self-refs (remove-nils (map #(:href (:attrs %1)) (html/select snippets #{ [:a] }))), url))]
    	(if (nil? hrefs)
    	  []
    		hrefs)))

(defn create-root-node
  [urls depth]
  (Webpage. "root" 0 nil depth urls (atom [])))

(defn fetch-page
  [url depth]
  (try
    (let [content (client/get url http-client-options)
          body (:body content)]
      (Webpage. url (:status content) body depth (get-hrefs body url) (atom [])))
    (catch Exception e (Webpage. url 404 nil 0 '() (atom [])))))

(defn crawl-node
  [parent node curr-depth]
  (if (not-nil? parent)
    (swap! (:children parent) conj node))
  (let [new-depth (dec curr-depth)]
    (if (>= new-depth 0)
      (doall (pmap (fn [elem] (crawl-node node (fetch-page elem new-depth) new-depth)) (:urls node))))
    node))
