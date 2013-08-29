(ns marmion.core
    (:require [instaparse.core :as insta]
              [marmion.util])
    (:require [aacc.core :refer :all]))


(def mar-hic (insta/parser (slurp "marmalade.grammar")))

(def mar-enl (insta/parser (slurp "marmalade.grammar") :output-format :enlive))

(def edn-enl (insta/parser (slurp "edn.grammar") :output-format :enlive))
                           
(def edn-hic (insta/parser (slurp "edn.grammar"))) 
                                  
(def ex-str (mar-enl (slurp "../exmp.md")))
                                  
(def edn edn-hic)

(def clj-mac (insta/parser (slurp "clj-macro.grammar") :output-format :enlive))

(def marm-str (slurp "../marmalade.md"))

(def parsed-marmalade (mar-hic marm-str))

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
  (if (map? (first e-tree))
      (tree-seq (comp seq :content) :content (first e-tree))
      (tree-seq (comp seq :content) :content e-tree)))
                                      
(defn- flatten-enlive
  "flattens an enlive tree (instaparse dialect)"
  [tree]
  (apply str (filter string? (e-tree-seq tree))))
                                         
(defn- flatten-hiccup
  "flattens a hiccup tree (instparse dialect)"
  [tree]
  (apply str (filter string? (flatten tree))))
                                      
(defn flat-tree
     [tree]
     (if (vector? tree)
         (flatten-hiccup tree)
         (flatten-enlive tree)))
                    
(defn re-parse
  "[parser tree (:rule)]
  Re-parse an instaparse tree with a parser
  If :rule is given, re-parse only those nodes matching
  :rule."
  ([parser tree]
  (if (vector? tree)
         (insta/parse parser (flatten-hiccup tree))
         (insta/parse parser (flatten-enlive tree))))
  ([parser tree rule]
  (if (vector? tree)
         (insta/transform {rule (fn [& node] (re-parse parser [rule node]))} tree)
         (insta/transform {rule (fn [& node] (re-parse parser {:tag rule, :content node}))} tree))))
               