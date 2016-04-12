(ns clojure-rest.db
  (:import com.mchange.v2.c3p0.ComboPooledDataSource) 
  (require [clojure.java.jdbc :as jdbc]
           [jdbc.pool.c3p0    :as pool]
           [clojure.java.jdbc :as sql]))

(def db-uri
  (java.net.URI. (or
                  (System/getenv "DATABASE_URL")
                  "postgresql://localhost:5432/clojure_test")))

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

(def pooled-db (delay (pool db-config)))
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
