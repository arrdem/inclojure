(ns inclojure.util
  (:use     [clojure.java.io :only (file reader)])
  (:require [clojure.string :as s]
            [taoensso.timbre.profiling :refer [defnp]]))

(defnp pos-files [which]
  (filter
    (every-pred #(.isFile %) (complement #(.isHidden %)))
    (file-seq (file (str "./penn-treebank3/tagged/pos/" which)))))

(defnp lines [[f & fs]]
  (lazy-seq
    (concat
      (line-seq (reader (.getPath f)))
      (if (seq fs) (lines fs)))))

(defnp metadata-sentence
  "between each sentence is some metadata about where in the corpus the line
  comes from:

    [ @8k1011sx-a-14/CD ]

  this fn identifies those."
  [[first-line & others]]
  (and
    (empty? others)
    (re-find #"^\[ \@" first-line)))

(defnp lines-to-labeled-tokens [lines]
  "the lines of a sentence look like this:

    List/VB
    [ the/DT flights/NNS ]
    ...

  this function takes a seq of lines and returns the (token, label) pairs.

  the logic is so gross. it seems like maybe ->> could clean it up, but the
  problem is the sequences are so nested that it's not a strait transformation"
  (map
    #(s/split % #"/")
    (flatten
      (map
        #(s/split (s/replace % #"^\[ (.*) \]$", "$1") #" ")
        lines))))

(def separator-line #(re-find #"^===" %))

(defnp sentences [lines]
  "return token/label pairs, grouped by sentence from the input"
  (->> (filter not-empty lines)
       (partition-by separator-line)
       (filter (complement #(and (= (count %) 1) (separator-line (first %)))))
       (filter (complement metadata-sentence))
       (map lines-to-labeled-tokens)))

(defnp labeled-tokens [[sentence & input-sentences]]
  "whereas `sentences` returns a list of lists of token/label pairs, this just
  returns a flat list of the token/label pairs"
  (lazy-seq
    (concat
      sentence
      (if (seq input-sentences) 
        (labeled-tokens input-sentences)))))

