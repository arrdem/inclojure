#!/usr/bin/env python

import sys, re
from time import time
from os import walk, makedirs
from os.path import join, exists
from functools import partial
from random import random

POS_DIR = './penn-treebank3/tagged/pos'
OUTPUT_DIR = 'sample-data/pos'
PARTITION_TOKEN = '============'
METADATA_LINE = re.compile(r'^\[ @')

def files(pattern):
    for root, dirs, files in walk(POS_DIR):
        for f in files:
            p = join(root, f)
            if pattern.search(p):
                yield p

atis_files = partial(files, re.compile(r'atis/'))
wsj_files = partial(files, re.compile(r'wsj/\d+/'))

def partitions(f, token=PARTITION_TOKEN):
    """
    partition a file by some token

    returns a generator of lists of lines
    """
    lines = []
    for line in f:
        if line.startswith(PARTITION_TOKEN) and lines:
            yield lines
            lines = []
        else:
            lines.append(line)
    yield lines

def tokens(p):
    """
    given a partition. note that this flattens the hierarchical penn
    representation

    return a generator of tokens
    """
    # filter out blank lines
    p = filter(lambda s: s.strip(), p)
    if not p or METADATA_LINE.search(p[0]):
        return
    for l in p:
        for t in l.strip().split():
            if '/' in t:
                yield tuple(t.rsplit('/', 1))

def all_tokens(input_files):
    for f in input_files:
        with open(f) as inf:
            for p in partitions(inf):
                for token in tokens(p):
                    yield token

def tokens_and_labels(tokens):
    ts = set()
    ls = set()
    for t, l in tokens:
        ts.add(t)
        ls.add(l)
    return ts, ls

def random_hmm(tokens, labels):
    ls = dict((l, dict(map(lambda t: (t, random()), tokens))) for l in labels)
    return {
        'tokens': tokens,
        'label-set': labels,
        'labels': ls
    }

def bench(which=None):
    if which and 'wsj' in which:
        files = wsj_files
    else:
        files = atis_files

    def token_seq():
        start = time()
        for t in all_tokens(files()): pass
        return time() - start

    def hmm():
        start = time()
        ret = random_hmm(*tokens_and_labels(all_tokens(files())))
        return time() - start

    avg = lambda s: sum(s) / len(s)
    times = 5

    token_seq()
    print "File I/O:", avg([token_seq() for i in range(times)])

    hmm()
    print "HMM:     ", avg([hmm() for i in range(times)])


if __name__ == '__main__':
    bench(sys.argv[1] if len(sys.argv) >= 1 else None)

