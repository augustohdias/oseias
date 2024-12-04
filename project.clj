(defproject oseias-master "0.1.0-SNAPSHOT"
  :description "Ironsworn RPG Movement Solver"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-core "1.9.6"]
                 [ring/ring-jetty-adapter "1.9.6"]
                 [metosin/reitit "0.5.18"]
                 [metosin/reitit-swagger "0.5.18"]
                 [ring/ring-json "0.5.1"]
                 [org.clojure/tools.namespace "1.3.0"]]
  :repositories [["central" {:url "https://repo1.maven.org/maven2/"}]
                 ["clojars" {:url "https://repo.clojars.org/"}]]
  :min-lein-version "2.0.0"
  :main ^:skip-aot oseias-master.main
  :target-path "target/%s"
  :uberjar-name "app.jar"
  :profiles {:uberjar {:aot :all}})
