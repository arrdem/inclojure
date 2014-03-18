# clojure/python perf comparison

this is a quick little test to investigate if clojure is appropriate for a
semester project of implementing a [hidden markov model (hmm)][hmm].

- [clojure implementation][clj-impl]
- [python implementation][py-impl]

## background info

the performance test involves reading about a million part-of-speech-tagged
sentences from disk (numerous files), and doing a minor bit of processing to
produce a random hmm for the data.

so there are two phases:

- reading the data from disk, and doing minor processing to convert it into a
  sequence of (word, part-of-speech) pairs ([clojure][clj-io], [python][py-io])
- making the actual model, which is a mapping for each part of speech that maps
  words to probabilities (floats) ([clojure][clj-hmm], [python][py-hmm])

the first is i/o intensive, the second involves creating very large map/dict
structures.

essentially the first step converts a bunch of files on disk that look like
this:

```text
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

...etc...
```

to a sequence of word/part of speech pairs that look like this:

```text
(list, VB)
(the, DT)
(flights, NNS)
(from, IN)
(Baltimore, NNP)

...etc...
```

and the second step converts that sequence to a set of maps from words to
(randomly generated) probabilities:

```js
{
  "VB": {
    "list": 0.233432928,
    "the": 0.984650376,
    "flights": 0.502483920,
    // ...etc...
  },
  "DT": {
    "list": 0.684650376,
    "the": 0.133432928,
    "flights": 0.702483920,
    // ...etc...
  },
  // ...etc...
}
```

## benchmark script

you should just be able to run:

    $ ./bin/bench

from the project's directory. this will compile the clojure and run the
benchmarks in each implementation. the python version is dramatically faster.

on a 2.5 GHz Intel Core i5 MacBook Pro with 16 GB of memory:

| Lang    | I/O Runtime (s) | HMM Runtime (s) |
| ------- | --------------- | --------------- |
| Clojure | 5.57            | 36.54           |
| Python  | 2.64            |  5.36           |

in every case the machine is warmed up and the given time is averaged across 5
runs.

## discussion

the problem with the clojure code seems to be that it is using all 4.0 Gb of
memory allotted to the java vm, while the python version never goes above 1.8
Mb or so. this is of course a significantly sized working set, but it is the
magnitude that one would expect when doing real-world(ish) nlp. i'm new to
clojure, so i'm probably just missing something.

**is there idiomatic clojure that can match the python performance for this
workload?**

[hmm]: http://en.wikipedia.org/wiki/Hidden_Markov_model
[clj-impl]: https://github.com/aaronj1335/inclojure/blob/master/src/inclojure/hmm.clj#L20
[py-impl]: https://github.com/aaronj1335/inclojure/blob/master/inclojure/__init__.py#L73
[clj-io]: https://github.com/aaronj1335/inclojure/blob/master/src/inclojure/util.clj#L56
[py-io]: https://github.com/aaronj1335/inclojure/blob/master/inclojure/__init__.py#L58
[clj-hmm]: https://github.com/aaronj1335/inclojure/blob/master/src/inclojure/hmm.clj#L20
[py-hmm]: https://github.com/aaronj1335/inclojure/blob/master/inclojure/__init__.py#L73
