(ns inclojure.hmm
  (:use [inclojure.util]
        [alex-and-georges.debug-repl]))

(defn tokens-and-labels [tokens]
  (reduce
    (fn [{:keys [tokens labels]} pair]
      {:tokens (conj tokens (first pair))
       :labels (conj labels (second pair))})
    {:tokens #{} :labels #{}}
    tokens))
