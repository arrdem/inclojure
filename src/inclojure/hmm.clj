(ns inclojure.hmm
  (:use [inclojure.util]
        [alex-and-georges.debug-repl]))

(defn tokens-and-labels [labeled-tokens]
  "given a sequence of token/label pairs, return a set of the tokens and a set
  of the labels"
  ; {:tokens (set (map first labeled-tokens))
  ;  :labels (set (map second labeled-tokens))})
  (reduce
    (fn [{:keys [tokens labels]} [token label]]
      {:tokens (conj tokens token)
       :labels (conj labels label)})
    {:tokens #{} :labels #{}}
    labeled-tokens))

(defn random-probs-for-tokens [tokens]
  (into {} (map #(vector % (rand)) tokens)))

(defn random-hmm [labeled-tokens]
  "create an hmm

  more formally, a set of N+2 states:

    S = {s_0,..., s_N, s_F}

  and M observations:

    V = {v_1,..., v_M}

  where `q_t = s` means that a given sequence was in state `s` at time `t`, and
  the parameters:

    λ = {A, B}

  where A is the state transition probability distribution:

    a_ij = P(q_t+1 = s_j | q_t = s_i)

  and B is the observation probability distribution for each state:

    b_j(k) = P(v_k at t | q_t = s_j)

  input:
   - labeled-tokens: a lazy seq of token/label pairs. something like:

    (('person' 'NOUN') ('run' 'VERB') ...)

  returns: a structure something like:

    {:tokens #{... set of tokens}
     :label-set #{'foo' ... rest of labels}
     :labels {
      'foo' {'bar' 0.123 ... rest of token/probability paris}
      ... rest of labels}}"
  (let [{:keys [tokens labels]} (tokens-and-labels labeled-tokens)]
    {:tokens tokens
     :label-set labels
     :labels (into {} (map #(vector % (random-probs-for-tokens tokens)) labels))}))
