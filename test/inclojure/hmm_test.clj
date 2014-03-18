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

(def expected-random-hmm
  {:tokens #{"a" "stop" "flights" "in" "that" "Seattle" "I" "from" "making"
             "serve" "flight" "the" "dinner" "leaving" "need" "Baltimore"
             "Does" "to" "List" "this" "Minneapolis"}
   :label-set #{"NN" "VBP" "IN" "WDT" "VB" "PRP" "VBZ" "DT" "NNP" "NNS" "TO"
                "VBG"}
   :labels {"NN" {"a" 1, "stop" 1, "flights" 1, "in" 1, "that" 1, "Seattle" 1,
                  "I" 1, "from" 1, "making" 1, "serve" 1, "flight" 1, "the" 1,
                  "dinner" 1, "leaving" 1, "need" 1, "Baltimore" 1, "Does" 1,
                  "to" 1, "List" 1, "this" 1, "Minneapolis" 1}
            "VBP" {"a" 1, "stop" 1, "flights" 1, "in" 1, "that" 1, "Seattle" 1,
                   "I" 1, "from" 1, "making" 1, "serve" 1, "flight" 1, "the" 1,
                   "dinner" 1, "leaving" 1, "need" 1, "Baltimore" 1, "Does" 1,
                   "to" 1, "List" 1, "this" 1, "Minneapolis" 1}
            "IN" {"a" 1, "stop" 1, "flights" 1, "in" 1, "that" 1, "Seattle" 1,
                  "I" 1, "from" 1, "making" 1, "serve" 1, "flight" 1, "the" 1,
                  "dinner" 1, "leaving" 1, "need" 1, "Baltimore" 1, "Does" 1,
                  "to" 1, "List" 1, "this" 1, "Minneapolis" 1}
            "WDT" {"a" 1, "stop" 1, "flights" 1, "in" 1, "that" 1, "Seattle" 1,
                   "I" 1, "from" 1, "making" 1, "serve" 1, "flight" 1, "the" 1,
                   "dinner" 1, "leaving" 1, "need" 1, "Baltimore" 1, "Does" 1,
                   "to" 1, "List" 1, "this" 1, "Minneapolis" 1}
            "VB" {"a" 1, "stop" 1, "flights" 1, "in" 1, "that" 1, "Seattle" 1,
                  "I" 1, "from" 1, "making" 1, "serve" 1, "flight" 1, "the" 1,
                  "dinner" 1, "leaving" 1, "need" 1, "Baltimore" 1, "Does" 1,
                  "to" 1, "List" 1, "this" 1, "Minneapolis" 1}
            "PRP" {"a" 1, "stop" 1, "flights" 1, "in" 1, "that" 1, "Seattle" 1,
                   "I" 1, "from" 1, "making" 1, "serve" 1, "flight" 1, "the" 1,
                   "dinner" 1, "leaving" 1, "need" 1, "Baltimore" 1, "Does" 1,
                   "to" 1, "List" 1, "this" 1, "Minneapolis" 1}
            "VBZ" {"a" 1, "stop" 1, "flights" 1, "in" 1, "that" 1, "Seattle" 1,
                   "I" 1, "from" 1, "making" 1, "serve" 1, "flight" 1, "the" 1,
                   "dinner" 1, "leaving" 1, "need" 1, "Baltimore" 1, "Does" 1,
                   "to" 1, "List" 1, "this" 1, "Minneapolis" 1}
            "DT" {"a" 1, "stop" 1, "flights" 1, "in" 1, "that" 1, "Seattle" 1,
                  "I" 1, "from" 1, "making" 1, "serve" 1, "flight" 1, "the" 1,
                  "dinner" 1, "leaving" 1, "need" 1, "Baltimore" 1, "Does" 1,
                  "to" 1, "List" 1, "this" 1, "Minneapolis" 1}
            "NNP" {"a" 1, "stop" 1, "flights" 1, "in" 1, "that" 1, "Seattle" 1,
                   "I" 1, "from" 1, "making" 1, "serve" 1, "flight" 1, "the" 1,
                   "dinner" 1, "leaving" 1, "need" 1, "Baltimore" 1, "Does" 1,
                   "to" 1, "List" 1, "this" 1, "Minneapolis" 1}
            "NNS" {"a" 1, "stop" 1, "flights" 1, "in" 1, "that" 1, "Seattle" 1,
                   "I" 1, "from" 1, "making" 1, "serve" 1, "flight" 1, "the" 1,
                   "dinner" 1, "leaving" 1, "need" 1, "Baltimore" 1, "Does" 1,
                   "to" 1, "List" 1, "this" 1, "Minneapolis" 1}
            "TO" {"a" 1, "stop" 1, "flights" 1, "in" 1, "that" 1, "Seattle" 1,
                  "I" 1, "from" 1, "making" 1, "serve" 1, "flight" 1, "the" 1,
                  "dinner" 1, "leaving" 1, "need" 1, "Baltimore" 1, "Does" 1,
                  "to" 1, "List" 1, "this" 1, "Minneapolis" 1}
            "VBG" {"a" 1, "stop" 1, "flights" 1, "in" 1, "that" 1, "Seattle" 1,
                   "I" 1, "from" 1, "making" 1, "serve" 1, "flight" 1, "the" 1,
                   "dinner" 1, "leaving" 1, "need" 1, "Baltimore" 1, "Does" 1,
                   "to" 1, "List" 1, "this" 1, "Minneapolis" 1}}})

(deftest tokens-and-labels
  (is (= (count (hmm/tokens-and-labels tokens-fixture))) 2)
  (is (= (hmm/tokens-and-labels tokens-fixture) expected-tokens-and-labels)))

(deftest random-probs-for-tokens
  (is (= (count (hmm/random-probs-for-tokens [:a :b :c :d])) 4))
  (is (=
       (set (keys (hmm/random-probs-for-tokens [:a :b :c :d])))
       #{:a :b :c :d}))
  (is (>= (:a (hmm/random-probs-for-tokens [:a :b :c :d])) 0.))
  (is (<= (:a (hmm/random-probs-for-tokens [:a :b :c :d])) 1.))
  (with-redefs [rand (constantly 1)]
    (is (= (hmm/random-probs-for-tokens [:a :b :c :d]) {:d 1 :c 1 :b 1 :a 1}))))

(deftest random-hmm
  (with-redefs [rand (constantly 1)]
    (is (= (hmm/random-hmm tokens-fixture) expected-random-hmm))))
