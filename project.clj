(defproject inclojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojars.gjahad/debug-repl "0.3.3"]]
  :aot [inclojure.core]
  :main inclojure.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[org.clojure/tools.namespace "0.2.4"]]
                   :source-paths ["dev"]}}
  :jvm-opts ["-Xmx 2G" "-server"]
  :repl-options {:init-ns inclojure.repl})
