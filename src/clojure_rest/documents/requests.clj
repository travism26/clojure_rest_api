(ns clojure-rest.documents.requests
  (:use compojure.core)
  (:use cheshire.core)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [clojure.java.jdbc :as sql]
            [compojure.route :as route]
            [clojure-rest.db :as db]))

(defn db-connection [] @db/pooled-db)

;(defn uuid [] "THIS IS SOME UUID I GUESS!")
(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn request []
  (str "THIS IS A DOC REQUEST"))



(defn get-all-documents []
  (response
   (sql/query (db-connection)
              ["select * from documents limit 25"])))

(defn get-document [id]
  (response
        (sql/query (db-connection)
                   ["select * from documents where id = ?" id]
                   (cond
                    (empty?) {:status 404}
                    :else (response (first))))))

(defn create-new-document [doc]
  (let [id (uuid)]
    (let [document (assoc doc "id" id)]
      (sql/insert! (db-connection) :documents document))
    (get-document id)))

(defn update-document [id doc]
  (let [document (assoc doc "id" id)]
    (sql/update! (db-connection)
     :documents ["id=?" id] document))
  (get-document id))

(defn delete-document [id]
  (sql/delete! (db-connection)
   :documents ["id=?" id])
  {:status 204})
