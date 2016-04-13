(ns clojure-rest.apicall
  (:require [clj-http.client :as client]))

; http://jsonplaceholder.typicode.com/posts/

(defn dummy-data [] 
  (client/get "http://jsonplaceholder.typicode.com/posts/" {:accept :json}))
