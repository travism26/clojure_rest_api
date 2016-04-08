(defproject clojure-rest "0.1.0-SNAPSHOT"
  :description "Travis Martin learning to build RESTFul api endpoints"
  :url "https://github.com/travism2"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-json "0.1.2"]
                 [c3p0/c3p0 "0.9.1.2"]
                 [org.clojure/java.jdbc "0.5.0"]
                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.1"]
;                [com.h2database/h2 "1.3.168"] ; only require for in mem DB
                 [org.postgresql/postgresql "9.4.1208.jre7"]
                 [cheshire "4.0.3"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler clojure-rest.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
