(ns primetab.primes
  "Generate the first `n` primes and printable multiplication matrices.
  See `primetab.primes-exp` for related experimental code."
  (:require
   [clojure.string :as str]
   [io.aviso.ansi :as coloring :refer [red]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Prime generators

(defn not-divisible?
  "Filter for candidates that don't evenly divide target.
  => (not-divisible? 7 3) -> true"
  [cand tgt]
  (not (zero? (mod cand tgt))))

(defn sieve
  "Generate infinite primes lazily via the Sieve of Eratosthenes, recursively.
  Initial input should be all the natural numbers excluding 1.
  Each subsequent call contains the `remaining-naturals` not removed by filtering.
  Wrap this in a `take` to avoid infinite output.

  For simplicity, there are a couple optimizations that this function
  does not attempt:
  - could skip to squares of each `n1`
  - could quit when square of `n1` is less than `remaining-naturals`"
  ([] (sieve (iterate inc 2))) ; intended public version is this no-arg call
  ([remaining-naturals]
   (let [n1 (first remaining-naturals)]
     (lazy-seq
      (cons n1
            (sieve (filter #(not-divisible? % n1)
                           (rest remaining-naturals))))))))

(defn primes-rdc
  "[NIU] A reducer that serves as one example method to generate primes.
  Returns the resulting sequence of `primes`, appending if new
  `cand`idate is prime."
  [primes cand]
  (if (some zero? (map (partial mod cand) primes))
    primes
    (conj primes cand)))

;; (time (primes-slow 200)) ; horrible! 1.5s
(defn primes-slow
  "[NIU] Another approach to generating primes.
  This is hacky in that it needs to limit the `iterate` and does so by
  squaring the actually desired limit `n`."
  ([] (primes-slow 10))
  ([n]
   (take n
         (reduce primes-rdc [2] (take (* n n) (iterate inc 3))))))

;; (def p10 (time (take-primes 3)))
(defn take-primes
  "Generate a sequence of `n` primes by calling a prime function, like `sieve`."
  ([n] (take-primes n sieve))
  ([n prime-fn]
   (take n (prime-fn))))

;; (prime-matrix 4)
(defn prime-matrix
  "Produce a matrix (multiplication table) of `n` sequences of primes.

  Example:
  => (prime-matrix 4)
  [[4 6 10 14]
   [6 9 15 21]
   [10 15 25 35]
   [14 21 35 49]]

  Input is either the number `n` of primes to be generated, or a
  sequence of already generated primes.

  NOTE: This does nearly twice as many calculations as is necessary,
  since it's a reflection matrix.
  "
  [n-or-primes]
  (let [primes (if (sequential? n-or-primes)
                 n-or-primes
                 ;; Just use the fast `sieve` algo.
                 (take-primes n-or-primes sieve))]
    (for [p primes]
      (for [q primes]
        (* p q)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Printing (see manual tests)

;; Shorthand functions for quickly getting delimiter/separator from options
(defn- del [opts]       (if (:csv opts) "," \tab))
(defn- colorizer [opts] (if (:bland opts) identity red))

;; (print-heading ["x" "y" "z"] {:bland false, :csv false})
(defn- print-heading
  "Print a matrix row heading."
  [headings opts]
  (when-not (:raw opts)
    (printf "%s%s\n"
            (del opts)
            ((colorizer opts) (str/join (del opts) headings)))))

;; (print-row 0 [1 2 3] {:num-primes 3, :bland false})
(defn- print-row
  "Print a row with optional label, color, etc."
  ;; NOTE: NIU, but better than the hacky all-in-one `tabulate`.
  [label row opts]
  (let [row (if (:raw opts)
              row
              (cons ((colorizer opts) label) row))]
    (print (str (str/join (del opts) row) "\n"))))

;; (print-matrix {:num-primes 4, :raw true, :bland false})
(defn print-matrix
  "Print a multiplication table of primes, while calculating them.
  Flexibly print row and column indicators (column headers, row
  labels) based on `opts`.  Possibilities include colorful, bland, or
  omission."
  ;; Another way to cleanly delimit is carefully spaced numbers where
  ;; the **length of the longest number plus one** determines the
  ;; field width.  And the headers/labels could use `---` and `|`.
  [opts]
  (let [n      (get opts :num-primes 10)
        primes (take-primes n)
        matrix (prime-matrix n)]
    (print-heading primes opts)
    (doall (map #(print-row %1 %2 opts) primes matrix))))
