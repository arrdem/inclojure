(ns inclojure.ngram
  (:use inclojure.util)
  (:require [taoensso.timbre.profiling :refer [defnp pspy]])
  (:gen-class))

(defn initial-model
  ([] (pspy :initial-model (initial-model :forward)))
  ([direction]
     (pspy :initial-model
           {:forward (if (not= direction :backward) true)
            :unigram {"<S>" 0 "</S>" 0 "<UNK>" 0}
            :bigram {}})))
  
(defnp start 
  [{:keys [forward]}] 
  (if forward "<S>" "</S>"))

(defnp end
  [{:keys [forward]}] 
  (if forward "</S>" "<S>"))

(defnp inc-token 
  [model-map k]
  (let [v (model-map k)]
    (assoc model-map k (inc v))))

(defnp add-token 
  [model token]
  (inc-token (:unigram model) token))

; (defn train-sentence [{:keys [unigram bigram forward] :as model} sentence]
;   (let [prev (start model)]
;     (reduce
;       (fn [model token]
;         )
;       model
;       (tokens sentence))))

; (defn train
;   ([sentences] (train sentences :forward))
;   ([sentences direction]
;    (reduce train-sentence (initial-model direction) sentences)))
