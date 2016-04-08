(ns clojure-rest.handler
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:use compojure.core)
  (:use cheshire.core)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [clojure.java.jdbc :as sql]
            [compojure.route :as route]
            [clojure-rest.db :as db]))

; full jdbcURL
;jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true
(def db-config
  {
   :classname "org.postgresql.Driver"
   ;:classname "org.h2.Driver"
   :subprotocol "postgresql"
   ;:subprotocol "h2"
   :subname "//localhost/clojure_test"
   ;:subname "mem:document" -> in memory database;
   :user "postgres"
   :password ""})

(defn pool
  [config]
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass (:classname config))
               (.setJdbcUrl (str "jdbc:" (:subprotocol config) ":" (:subname config)))
               (.setUser (:user config))
               (.setPassword (:password config))
               (.setMaxPoolSize 1)
               (.setMinPoolSize 1)
               (.setInitialPoolSize 1))]
    {:datasource cpds}))

(def pooled-db (delay (pool db/db-config)))

(defn db-connection [] @pooled-db)

;check if the table schemas are created.
(defn db-schema-migrated?
  []
  (-> (sql/query (db-connection)
                 [(str "select count(*) from information_schema.tables where table_name = 'documents' ")])
      first :count pos?))

(when (not(db-schema-migrated?))
 (sql/db-do-commands (db-connection)
  ;(sql/drop-table :documents) ; no need to do that for in-memory databases
   (sql/create-table-ddl
    :documents 
    [:id "varchar(256)" "primary key"]
    [:title "varchar(1024)"]
    [:text :varchar])))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn get-all-documents []
  (response
   (sql/query (db-connection)
              ["select * from documents limit 25"])))

(defn get-document [id]
  (into [] 
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

(defroutes app-routes
  (context "/api/travis" [] (defroutes documents-routes
        (GET  "/" [] (get-all-documents))
        (POST "/" {body :body} (create-new-document body))
        (GET "/test" [] "what's up buddy!")
        (context "/:id" [id] 
                 (defroutes document-routes
                   (GET    "/" [] (get-document id))
                   (PUT    "/" {body :body} (update-document id body))
                   (DELETE "/" [] (delete-document id))))))
  (route/not-found "Not Found dude"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))


;LEARNING CLOJURE! dont judge :)
;; (ns clojure-rest.handler
;;   (:require [compojure.core :refer :all]
;;             [compojure.route :as route]
;;             [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
;;             ))
