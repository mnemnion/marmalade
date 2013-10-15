(ns marmion.arachne
  (:require [instaparse.core :as insta]
            [marmion.util :refer :all]))

(def ^:private arachne-parse
  (insta/parser (slurp "arachne.grammar") :output-format :enlive :total true))

(def clj-mac (insta/parser (slurp "clj-macro.grammar") :output-format :enlive))

(def ^:private  macro-parser
  (insta/parser (slurp "macro.grammar") :output-format :enlive))

(defn- fix-final-line
  "fix the final line of a Marmalade file if necessary"
  [tree]
  (insta/transform
   {:final-line
    (fn
      [& chars]
      (assoc {:tag :prose} :content (list (apply str chars))))}
   tree))

(defn arachnify
  "takes a string and arachnifies it"
  [source]
  (-> source arachne-parse fix-final-line))

(defn macro-parse-tree
  "parse macros in-place on a single tree"
  [tree]
  (re-parse macro-parser tree :macro))


(defn clj-parse
  "clojure macro parses appropriate code."
  [tree]
  (if (= (code-type tree) "clojure")
    (re-parse clj-mac tree :code-body)
    (if (= (code-type tree) "grammar")
      (re-parse clj-mac tree :code-body)
      tree)))

(defn strip-codes
  "takes an Arachne parse tree and returns a list of all code blocks, with macros (in Clojure blocks) parsed."
  [tree]
  ;;note that this lacks a certain generality
  (map macro-parse-tree (map clj-parse (tag-stripper :code tree))))

(defn code-parse
  "Strips codes from arachne-parsed trees and macro-parses them"
  [md-tree-list]
  (->> md-tree-list
       (map strip-codes)
       flatten
       (map macro-parse-tree)
       (map #(smush :literal-code %))))
