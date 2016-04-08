(ns clojure-rest.db
 ; (:import com.mchange.v2.c3p0.ComboPooledDataSouce)
  
  (require [clojure.java.jdbc :as jdbc]
           [jdbc.pool.c3p0    :as pool]))


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

