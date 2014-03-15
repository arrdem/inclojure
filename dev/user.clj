(ns inclojure.repl
  (:require [clojure.java.io :as io]
            [clojure.tools.namespace.repl :refer (refresh-all)]
            [clojure.test])
  (:use [clojure.reflect :only [reflect]]
        [clojure.pprint :only [pprint print-table pp]]
        [inclojure.core]))

(clojure.tools.namespace.repl/disable-unload!)
(clojure.tools.namespace.repl/disable-reload!)

(defn t "run all unit tests for the project" []
  (refresh-all)
  (clojure.test/run-all-tests #".*inclojure.*"))

(defn r
  "inspect all of the properties in a java object, optionally by specifying a
  pattern"
  ([o] (r o "."))
  ([o prefix]
    (->> (reflect o :ancestors true)
         :members
         (filter #(re-find (re-pattern (str "(?i)" prefix)) (str (:name %))))
         (pprint))))

(refresh-all)
