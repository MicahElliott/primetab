(ns primetab.primes-test
  (:require [primetab.primes :as sut]
            [clojure.test :as t :refer [deftest testing is are]]
            [clojure.pprint :as pp]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Unit tests

(deftest sample-is-prime
  (let [p5 (sut/take-primes 5)]
    (testing "a prime matches what we expect"
      (is (= 7
             (nth p5 3))))
    (testing "correct number of primes generated"
      (is (= 5
             (count p5))))
    ))

(deftest good-row-of-4x4
  (is (= [14 21 35 49]
         (last (sut/prime-matrix 4)))))

;; (sut/take-primes 100)
(def p100
  "Set of first verified 100 primes"
  #{2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89
    97 101 103 107 109 113 127 131 137 139 149 151 157 163 167 173 179
    181 191 193 197 199 211 223 227 229 233 239 241 251 257 263 269 271
    277 281 283 293 307 311 313 317 331 337 347 349 353 359 367 373 379
    383 389 397 401 409 419 421 431 433 439 443 449 457 461 463 467 479
    487 491 499 503 509 521 523 541})

(defn prime? [n]
  (contains? p100 n))

(deftest rand-prime
  (testing "a random prime in small range is definitely prime"
   (is (prime? (last (sut/take-primes (rand-int 100)))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Integration tests

(deftest ^:integration lotsa-primes
  (testing "can generate a ton of primes"
    (is (= 1000
           (time (count (sut/take-primes 1000)))))))

(deftest ^:integration big-primes
  (testing "an arbitrary 'big' prime is realized/generated"
    (is (= 7919
           (last (sut/take-primes 1000)))))
  (testing "largest prime on my system with default JVM settings before StackOverflowError :)"
    (is (= 14519 ; ~500 ms
           (last (sut/take-primes 1700))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Manual tests: timing, slowness, printing
(comment
  (sut/tabulate {:number 8, :bland true, :labels true})
  (sut/tabulate {:number 8, :bland false})
  (time (take 10 (lazy-seq (reduce primes-rdc [2] (take 100 (iterate inc 3))))))
  (time (last (take 20 (reductions primes-rdc [2] (take 500 (iterate inc 3))))))

  (take 20 (sut/sieve (range 2 10)))
  ;; .07 ms!
  (time (take 500000000 (sut/sieve (iterate inc 2))))
  (take 5 (sut/sieve))


  )
