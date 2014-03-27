(ns inclojure.core
   (:require [inclojure.util :as util]
             [inclojure.hmm :as hmm]
             [taoensso.timbre.profiling 
              :refer [profile defnp]])
   (:gen-class))

(defmacro timeit
  "like clojure.core/time except it evaluates any lazy-seq's, returns the
  msecs passed, and ignores the output"
  [expr]
  `(let [start# (. System (nanoTime))]
     {:value ~expr
      :ms (-> (. System nanoTime)
              (- start#)
              (/ 1000000000.0))}))

(def times 5)

(defn token-seq [corpus]
  (-> (util/pos-files corpus)
      (util/lines)
      (util/sentences)
      (util/labeled-tokens)
      (timeit)))

(defn random-hmm [corpus]
  (-> (util/pos-files corpus)
      (util/lines)
      (util/sentences)
      (util/labeled-tokens)
      (hmm/random-hmm)
      (timeit)))

(defnp avg [x]
  (/ (apply + x) 
     (count x)))

(defn bench [f corpus]
  ; warm up the system
  (f corpus)
  (let [s (->> (for [i (range 5)] 
                 (f corpus))
               (map :ms))
        sp (doall s)]
  (avg sp)))

(defn -main
  [corpus & args]
  (do (println "File I/O: ")
      (profile :info :token-seq (bench token-seq corpus))
      (println "--------------------"))
  
  (do (println "HMM:      ")
      (profile :info :random-hmm (bench random-hmm corpus))
      (println "--------------------"))
  (. System exit 0))
      
