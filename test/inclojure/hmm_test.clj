(ns inclojure.hmm-test
  (:require [clojure.test :refer :all]
            [inclojure.util :as util]
            [inclojure.hmm :as hmm]
            [inclojure.util-test :as util-test]))

(def tokens-fixture
  (util/labeled-tokens (util/sentences util-test/pos-file-test-fixture)))

(def expected-tokens-and-labels
  {:tokens #{"a" "stop" "flights" "in" "that" "Seattle" "I" "from" "making"
             "serve" "flight" "the" "dinner" "leaving" "need" "Baltimore"
             "Does" "to" "List" "this" "Minneapolis"}
   :labels #{"NN" "VBP" "IN" "WDT" "VB" "PRP" "VBZ" "DT" "NNP" "NNS" "TO"
             "VBG"}})

(deftest tokens-and-labels
  (is (= (count (hmm/tokens-and-labels tokens-fixture))) 2)
  (is (= (hmm/tokens-and-labels tokens-fixture) expected-tokens-and-labels)))

; (hmm/tokens-and-labels (util/labeled-tokens (util/sentences (util/lines (util/pos-files "atis")))))
