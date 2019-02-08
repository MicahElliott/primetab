(ns primetab.core-test
  "Not going to really test the CLI driving code that comrpises core."
  (:require [clojure.test :refer [deftest testing is]]
            [primetab.core :refer :all]))

(deftest a-test
  (testing "DUMMY: no tests in core"
    (is (= 1 1))))
