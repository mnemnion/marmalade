(ns marmion.util
  (:require [instaparse.core :as insta]))


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
(defn e-tree-seq 
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
