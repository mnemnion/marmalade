(ns marmion.core
    (:require [instaparse.core :as insta]
              [marmion.util])
    #_(:require [aacc.core :refer :all]))

(def marm (insta/parser (slurp "marmalade.grammar") :output-format :enlive))

(def arachne-parse (insta/parser (slurp "arachne.grammar") :output-format :enlive))

(def marm-arach (arachne-parse marm-str))

(def edn (insta/parser (slurp "edn.grammar") :output-format :enlive))

(def edn-hic (insta/parser (slurp "edn.grammar")))

(def ex-str (marm (slurp "../exmp.md")))

(def clj-mac (insta/parser (slurp "clj-macro.grammar") :output-format :enlive))

(def macro-parse (insta/parser (slurp "macro.grammar") :output-format :enlive))

(def marm-str (slurp "../marmalade.md"))

(def parsed-marmalade (marm marm-str))

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


#_(def code-rule
  (def-rule-fn
    (println "here")
    (assoc state :codes (rest seq-tree))))

#_(def code-map {:code code-rule})

(defn code-stripper
  "strips :code"
   [seq-tree]
  (filter #(= :code (:tag %))
          seq-tree))

(defn tag-stripper
  "strips :tag from tree"
  [tag parse-tree]
  (let [seq-tree (e-tree-seq parse-tree)]
    (filter #(= tag (:tag %))
            seq-tree)))

(def marm-seq (e-tree-seq parsed-marmalade))

(map (partial re-parse clj-mac) (tag-stripper :code ex-str) ) ; works

(def marm-codes (map (partial re-parse clj-mac) (tag-stripper :code parsed-marmalade)))
                                        ;breaks in the second code block

(def m-codes (tag-stripper :code marm-arach))

(defn code-type
  "expects a :code tree. Returns the type of the code,
as a string, or \\n if not found."
  [tree]
  (first (:content (first (:content tree)))))

(defn clj-parse
  "clojure macro parses appropriate code."
  [tree]
  (if (= (code-type tree) "clojure")
    (re-parse clj-mac tree :code-body)
     tree))
