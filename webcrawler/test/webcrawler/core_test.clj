(ns webcrawler.core-test
  (:require [clojure.test :refer :all]
            [webcrawler.core :refer :all])
  (:require [clj-http.client :as client])
  (:use webcrawler.crawler)
  (import [java.net UnknownHostException]))

(deftest test-parse-html
  (testing "Parses HTML"
    (let [body (slurp "test/testdata/testpage.html")
          hrefs (get-hrefs body "http://test-foo.com")]
      (is (= (count hrefs) 3)))))

(deftest test-not-found
  (testing "Handles 404"
    (with-redefs-fn {#'client/get (fn [a b] (throw UnknownHostException))}
      #(is (= (:status (fetch-page "http://google.com/" 2)) 404)))))
