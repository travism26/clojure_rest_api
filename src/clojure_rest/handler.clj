(ns clojure-rest.handler
 ; (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:use compojure.core)
  (:use cheshire.core)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            ;[clojure.java.jdbc :as sql]
            [compojure.route :as route]
            ;[clojure-rest.db :as db]
            [clojure-rest.documents.requests :as doc]))

; full jdbcURL
;jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true
;; (def db-config7258354685694424
;;   {
;;    :classname "org.postgresql.Driver"
;;    ;:classname "org.h2.Driver"
;;    :subprotocol "postgresql"
;;    ;:subprotocol "h2"
;;    :subname "//localhost/clojure_test"
;;    ;:subname "mem:document" -> in memory database;
;;    :user "postgres"
;;    :password ""})

;(def pooled-db (delay (pool db/db-config)))

;(defn db-connection [] @db/pooled-db)

;check if the table schemas are created.
;; (defn db-schema-migrated?
;;   []
;;   (-> (sql/query (db-connection)
;;                  [(str "select count(*) from information_schema.tables where table_name = 'documents' ")])
;;       first :count pos?))

;; (when (not(db-schema-migrated?))
;;  (sql/db-do-commands (db-connection)
;;   ;(sql/drop-table :documents) ; no need to do that for in-memory databases
;;    (sql/create-table-ddl
;;     :documents 
;;     [:id "varchar(256)" "primary key"]
;;     [:title "varchar(1024)"]
;;     [:text :varchar])))

(defroutes app-routes
  (context "/api/travis" [] (defroutes documents-routes
        (GET  "/" [] (doc/get-all-documents))
        (POST "/" {body :body} (doc/create-new-document body))
        (GET "/test" [] "what's up buddy!")
        (GET "/request" [] (doc/request))
        (context "/:id" [id] 
                 (defroutes document-routes
                   (GET    "/" [] (doc/get-document id))
                   (PUT    "/" {body :body} (doc/update-document id body))
                   (DELETE "/" [] (doc/delete-document id))))))
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
