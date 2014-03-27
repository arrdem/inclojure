(ns inclojure.core
   (:require [inclojure.util :as util]
             [inclojure.hmm :as hmm])
  (:gen-class))

(defmacro timeit
  "like clojure.core/time except it evaluates any lazy-seq's, returns the
  msecs passed, and ignores the output"
  [expr]
  `(let [start# (. System (nanoTime))
         ret# (doall ~expr)]
     {:value ret#
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

(defn avg [x]
  (/ (apply + x) (count x)))

(defn bench [f corpus]
  ; warm up the system
  (f corpus)
  (avg (map :ms (repeatedly times (partial f corpus)))))

(defn -main
  [corpus & args]
  (println (str "File I/O: " (bench token-seq corpus)))
  (println (str "HMM:      " (bench random-hmm corpus))))
