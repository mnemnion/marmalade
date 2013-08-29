
(ns athena.core-test
  (:use clojure.test
        athena.core))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))


(def test-vec (apply vector (clojure.string/split (slurp "test-files.txt") #"\n")))

(def fail-vec (apply vector (clojure.string/split (slurp "fail-files.txt") #"\n")))

(defn count-failures [x] (count (insta/parses edn (slurp (str "/Users/atman/code/opp/edn-tests/invalid-edn/" x)))))
                                                  
(defn count-parses [x] (count (insta/parses edn (slurp (str "/Users/atman/code/opp/edn-tests/valid-edn/" x)))))
