(ns inclojure.util-test
  (:require [clojure.test :refer :all]
            [inclojure.util :as util]
            [clojure.string :refer [split-lines]]))

(def pos-file-test-fixture (split-lines "

[ @0y0012sx-a-11/CD ]

======================================

List/VB 
[ the/DT flights/NNS ]
from/IN 
[ Baltimore/NNP ]
to/TO Seattle/NNP 
[ that/WDT stop/VBP ]
in/IN 
[ Minneapolis/NNP ]

======================================

[ @0y0022sx-d-5/CD ]

======================================

Does/VBZ 
[ this/DT flight/NN ]
serve/VB 
[ dinner/NN ]

======================================

[ @8k1011sx-a-14/CD ]

======================================

[ I/PRP ]
need/VBP 
[ a/DT flight/NN ]
to/TO Seattle/NNP leaving/VBG from/IN 
[ Baltimore/NNP ]
making/VBG 
[ a/DT stop/NN ]
in/IN 
[ Minneapolis/NNP ]

======================================
"))

(deftest pos-files
  (is (= (count (util/pos-files "atis")) 1))
  (is (= (count (util/pos-files "wsj")) 2138))
  (is (every? = (map vector
                     ["atis3.pos"]
                     (map #(.getName %) (util/pos-files "atis"))))))

(deftest lines
  (is (= (count (util/lines (util/pos-files "atis"))) 7077))
  (is (= (class (util/lines (util/pos-files "atis"))) clojure.lang.LazySeq)))

(deftest metadata-sentence
  (is (util/metadata-sentence [(nth pos-file-test-fixture 2)])))

(deftest lines-to-labeled-tokens
  (is (=
       (util/lines-to-labeled-tokens (take 3 (drop 6 pos-file-test-fixture)))
       '(["List" "VB"] ["the" "DT"] ["flights" "NNS"] ["from" "IN"]))))

(deftest sentences
  (is (= (class (util/sentences pos-file-test-fixture)) clojure.lang.LazySeq))
  (is (= (count (util/sentences pos-file-test-fixture)) 3))
  (is (= (count (util/sentences (butlast pos-file-test-fixture))) 3))
  (is (= (count (util/sentences (util/lines (util/pos-files "atis")))) 577))
  (is (= (first (first (util/sentences pos-file-test-fixture))) ["List" "VB"]))
  )

(deftest tokens
  (is (=
       (count (util/labeled-tokens (util/sentences pos-file-test-fixture)))
       30))
  (is (=
       (first (util/labeled-tokens (util/sentences pos-file-test-fixture)))
       ["List" "VB"])))
