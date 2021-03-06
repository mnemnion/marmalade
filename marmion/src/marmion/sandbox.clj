
(ns marmion.sandbox
    (:require [instaparse.core :as insta]))


(def marmalade-parser (insta/parser (slurp "marmalade.grammar")))

(def mar-enl (insta/parser (slurp "marmalade.grammar") :output-format :enlive))

(def edn-enl (insta/parser (slurp "edn.grammar") :output-format :enlive))
                           
(def edn-hic (insta/parser (slurp "edn.grammar")))

(def edn edn-hic)
  
(def parsed-migraine (marmalade-parser (slurp "migraine.md")))

(def test-vec (apply vector (clojure.string/split (slurp "test-files.txt") #"\n")))

(def fail-vec (apply vector (clojure.string/split (slurp "fail-files.txt") #"\n")))

(defn count-failures [x] (count (insta/parses edn (slurp (str "/Users/atman/code/opp/edn-tests/invalid-edn/" x)))))
                                                  
(defn count-parses [x] (count (insta/parses edn (slurp (str "/Users/atman/code/opp/edn-tests/valid-edn/" x)))))

(defn key-maker
  "makes a keyword name from our file string"
  [file-name]
  (keyword  (last (clojure.string/split file-name #"/"))))

(defn smush
  "smushes a keyword corresponding to a terminal node"
  [rule tree]
    (insta/transform 
         {rule (fn [ & lines ] [rule (apply str lines)])} 
                   tree))
(defn- e-tree-seq 
  "tree-seqs enlive trees/graphs, at least instaparse ones"
  [e-tree]
  (tree-seq (comp seq :content) :content (first e-tree))) 

(defn flatten-rule-enlive
  "flattens an enlive tree (instaparse dialect)"
  [tree]
  (apply str (filter string? (e-tree-seq tree))))
                                         
(defn flatten-rule-hiccup
  "flattens a hiccup tree (instparse dialect)"
  [tree]
  (apply str (filter string? (flatten tree))))                                          

(defn weave-zeus
  "a weaver to generate our next iteration"
  [state code]
  (if (keyword? (first code))
      (if (= :magic-word (first code))
          (weave-zeus (assoc state 
                             :current-file, (first (rest code))) 
                      (drop 2 code))
          (let [file-key (key-maker (:current-file state))]
              (weave-zeus (assoc state
                                 file-key, 
                                 (apply str (state file-key) (first (rest (rest code))))) 
                          (drop 3 code))))                                                      
      state))
