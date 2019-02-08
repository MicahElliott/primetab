(ns primetab.primes
  "Generate the first `n` primes."
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
  ([] (sieve (iterate inc 2)))
  ([remaining-naturals]
   (let [n1 (first remaining-naturals)]
     (lazy-seq
      (cons n1
            (sieve (filter #(not-divisible? % n1)
                           (rest remaining-naturals))))))))

(defn primes-rdc
  "[NIU] A reducer that serves as one example method to generate primes.
  Returns the resulting sequence of primes, appending if new `cand`idate
  is prime."
  [primes cand]
  (if (some zero? (map (partial mod cand) primes))
    primes
    (conj primes cand)))

;; (time (primes2 20)) ; horrible!
(defn primes2
  "[NIU] Another approach to generating primes.
  This is hacky in that it needs to limit the `iterate` and does so by
  squaring the actually desired limit `n`."
  ([] (primes2 10))
  ([n]
   (take n
         (reduce primes-rdc [2] (take (* n n) (iterate inc 3))))))

;; (def p10 (time (take-primes 3)))
(defn take-primes
  "Generate a sequence of `n` primes by calling a prime function, like `sieve`."
  ([n] (take-primes n sieve))
  ([n prime-fn]
   (take n (prime-fn (iterate inc 2)))))

;; (prime-matrix 4)
(defn prime-matrix
  "Produce a matrix (multiplication table) of n sequences of primes.
  This would be a better way to feed the printer, but NIU.

  Example:
  => (prime-matrix 4)
  [[4 6 10 14]
   [6 9 15 21]
   [10 15 25 35]
   [14 21 35 49]]

  This does nearly twice as many calculations as is necessary, since
  it's a reflection matrix.
  "
  [n]
  (let [primes (take-primes n sieve)]
    (for [p primes]
      (for [q primes]
        (* p q)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Printing (not tested)

;; (print-row [1 2 3] {:num-primes 3, :bland true})
(defn- print-row
  "Print a row with optional label."
  ;; NOTE: NIU, but better than the hacky all-in-one `tabulate`.
  [row options]
  (let [colorize (if (:bland options) red identity)
        ;; if-let would be better but can't use binding in else condition
        row      (let [l (:raw options)]
                   (if l row (cons (colorize l) row)))]
    (print (str/join "\t" row))))

(defn- print-label
  "Flexibly print labeling based on `opts`.
  Possibilities include colorful, bland, or nothing."
  [opts s pre post]
  (let [printfn (if (:raw opts) (constantly nil) print)
        labelfn (if (:raw opts)
                  (constantly nil)
                  (if (:bland opts) identity red))]
    (if pre
      (printfn pre (labelfn s) post)
      (printfn (labelfn s) post))))

(defn tabulate
  "Print a multiplication table of primes, while calculating them."
  ;; TODO: leverage `prime-matrix` instead of mixing presentation and
  ;;       calculation as is done here.

  ;; Another way to cleanly delimit is carefully spaced numbers where
  ;; the **length of the longest number plus one** determines the
  ;; field width.  And the headers/labels could use `---` and `|`.
  [opts]
  (let [del    (if (:csv opts) "," \tab) ; BUG: CSV has bad spacing
        primes (take-primes (:num-primes opts))]
    (print-label opts (str/join del primes) del "\n")  ; headings
    (doseq [p primes]
      (print-label opts p nil del) ; labels
      (doseq [q primes]
        (print (* p q) del)) ; calculate each cell
      (println))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Printing playground
(comment
  (tabulate {:num-primes 8, :bland false, :raw false})
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Primes playground/experiments and early attempts

(comment
  (defn sieve2 [max]
    (let [cands (range 2 max)]
      (filter (fn [tgt cand] (zero? (mod cand tgt))) cands)))

  (defn s1 [tgt all]
    (filter (fn [cand] (zero? (mod cand tgt))) all))

  (filter #(not-divisible? % 2) (range 3 12))
  (filter #(not-divisible? % 3) (range 4 12))
  (filter #(not-divisible? % 4) (range 5 12))

  (def i 3)
  (def end 12)
  (filter #(not-divisible? % i) (range (inc i) end))

  (defn x [i end]
    (filter #(not-divisible? % i) (range (inc i) end)))
  (x 3 12)
  (x 4 12)

  (defn sieve [remaining]
    (let [tgt (first remaining)]
      (cons (first remaining)
            (sieve (x tgt 20)))))

  ;; Fun way to see nils after the 10 limit
  (sieve (range 2 10))

  (map #(not-divisible? % 2) (range 3 12))
  (map #(not-divisible? % 3) (range 4 12))

  (s1 2 (range 2 10))
  (let [all (range 2 10)
        half (range 2 5)]
    (map #(s1 % all) all))

  (some zero? [1 0 2])
  (primes-rdc [2 3 5] 7)
  (primes-rdc [2 3 5] 8)
  )
