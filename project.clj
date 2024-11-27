(defproject oseias-master "0.1.0-SNAPSHOT"
  :description "Ironsworn RPG Movement Solver"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring "1.9.6"]
                 [compojure "1.6.3"]
                 [ring/ring-json "0.5.1"]
                 [metosin/reitit "0.5.18"]
                 [metosin/reitit-swagger "0.5.18"]]
  :plugins [[lein-auto "0.1.3"]]
  :auto {:paths ["src" "resources"]}
  :main ^:skip-aot oseias-master.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
