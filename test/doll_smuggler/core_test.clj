(ns doll-smuggler.core-test
  (:require [clojure.test :refer :all]
            [doll-smuggler.core :refer :all]))

(def available-dolls (get-dolls "resources/available-dolls.txt"))
(def expected-dolls (get-dolls "test/resources/expected-dolls.txt"))
(def max-weight 400)

(deftest maximum-weight-test
  (testing "Is actual weight for accepted dolls less than maximum specified from challenge (400)?"
    (let [accepted-dolls (doll-smuggler available-dolls max-weight)]
      (is (<= (reduce + (map :weight accepted-dolls)) max-weight)))))

(deftest accepted-dolls-test
  (let [expected-names (sort (map :name expected-dolls))]
    (testing "Do actual accepted dolls match expected results from challenge?"
      (let [accepted-dolls (doll-smuggler available-dolls max-weight)]
        (is (= (sort (map :name accepted-dolls)) expected-names))))))

(deftest empty-set-test
  (testing "Is set of accepted dolls empty for a nil input?"
    (let [accepted-dolls (doll-smuggler nil max-weight)]
      (is (= (count accepted-dolls) 0))))
  (testing "Is set of accepted dolls empty for an empty input?"
    (let [accepted-dolls (doll-smuggler [] max-weight)]
      (is (= (count accepted-dolls) 0)))))

(deftest zero-maximum-weight-test
  (testing "Is set of accepted dolls empty for a zero maximum weight?"
    (let [accepted-dolls (doll-smuggler available-dolls 0)]
      (is (= (count accepted-dolls) 0)))))
