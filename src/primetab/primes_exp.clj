(ns primetab.primes-exp
  "Playground for experimenting and holding original attempts at
  fleshing out clean functions."
  (:require
   [primetab.primes :as primes]
   [io.aviso.ansi :as coloring :refer [red]]
   [clojure.string :as str]))

(defn- print-marker
  "Flexibly print row and column indicators (column headers, row
  labels) based on `opts`.  Possibilities include colorful, bland, or
  omission."
  [opts s pre post]
  (let [printfn  (if (:raw opts) (constantly nil) printf)
        markerfn (if (:raw opts)
                   (constantly nil)
                   (if (:bland opts) identity red))]
    (if pre
      (printfn "%s%s%s" pre (markerfn s) post)
      (printfn "%s%s"       (markerfn s) post))))

(defn tabulate
  "Print a multiplication table of primes, while calculating them."
  ;; TODO: leverage `prime-matrix` instead of mixing presentation and
  ;;       calculation as is done here.

  [opts]
  (let [d      (primes/del opts)
        primes (primes/take-primes (:num-primes opts))]
    (print-marker opts (str/join d primes) d "\n")  ; headings
    (doseq [p primes]
      (print-marker opts p nil d) ; labels
      (doseq [q primes]
        (printf "%s%s" (* p q) d)) ; calculate each cell
      (println))))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Primes playground/experiments and early attempts

(comment
  (defn sieve2 [max]
    (let [cands (range 2 max)]
      (filter (fn [tgt cand] (zero? (mod cand tgt))) cands)))

  (defn s1 [tgt all]
    (filter (fn [cand] (zero? (mod cand tgt))) all))

  (filter #(primes/not-divisible? % 2) (range 3 12))
  (filter #(primes/not-divisible? % 3) (range 4 12))
  (filter #(primes/not-divisible? % 4) (range 5 12))

  (def i 3)
  (def end 12)
  (filter #(primes/not-divisible? % i) (range (inc i) end))

  (defn x [i end]
    (filter #(primes/not-divisible? % i) (range (inc i) end)))
  (x 3 12)
  (x 4 12)

  (defn sieve [remaining]
    (let [tgt (first remaining)]
      (cons (first remaining)
            (sieve (x tgt 20)))))

  ;; Fun way to see nils after the 10 limit
  (primes/sieve (range 2 10))

  (map #(primes/not-divisible? % 2) (range 3 12))
  (map #(primes/not-divisible? % 3) (range 4 12))

  (s1 2 (range 2 10))
  (let [all (range 2 10)
        half (range 2 5)]
    (map #(s1 % all) all))

  (some zero? [1 0 2])
  (primes/primes-rdc [2 3 5] 7)
  (primes/primes-rdc [2 3 5] 8)
  )
