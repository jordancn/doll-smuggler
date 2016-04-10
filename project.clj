(defproject doll-smuggler "0.1.0-SNAPSHOT"
  :description "Maximize the total street value of drugs delivered"
  :url "https://github.com/jordancn/doll-smuggler"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"] [org.clojure/tools.cli "0.3.3"]]
  :main ^:skip-aot doll-smuggler.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
