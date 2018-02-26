(ns pegthing.core-test
  (:require [clojure.test :refer :all]
            [pegthing.core :refer :all]))

(deftest tri-test
  (testing "Tri Test"
    (is (= (last (take 1 tri)) 1))
    (is (= (last (take 3 tri)) 6))
    (is (= (last (take 6 tri)) 21))))

(deftest triangular-test
  (testing "Triganular returns true on triangle numbers"
    (is (= (triangular? 1) true))
    (is (= (triangular? 3) true))
    (is (= (triangular? 6) true))
    (is (= (triangular? 5) false))))

(deftest row-tri-test
  (testing "Row-tri gets the last tri of a given length"
    (is (= (row-tri 1) 1))
    (is (= (row-tri 3) 6))
    (is (= (row-tri 4) 10))))

(deftest row-num-test
  (testing "Row num returns the row that contains a tri"
    (is (= (row-num 1) 1))
    (is (= (row-num 6) 3))
    (is (= (row-num 10) 4))))

(deftest connect-test
  (testing "Connects two items in a board"
    (is (= (connect {} 10 1 3 6)
           {1 {:connections {6 3}}, 6 {:connections {1 3}}}))))
