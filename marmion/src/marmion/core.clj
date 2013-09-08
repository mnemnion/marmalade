(ns marmion.core
  (:require [instaparse.core :as insta]
            #_[marmion.arachne :refer :all]
            [marmion.util :refer :all]))

(def marm (insta/parser (slurp "marmalade.grammar") :output-format :enlive))

(def arachne-parse (insta/parser (slurp "arachne.grammar") :output-format :enlive))

(def edn (insta/parser (slurp "edn.grammar") :output-format :enlive))

(def edn-hic (insta/parser (slurp "edn.grammar")))

(def link-hunter (insta/parser (slurp "link-hunter.grammar") :output-format :enlive))

(def ex-str (marm (slurp "../exmp.md")))

(def clj-mac (insta/parser (slurp "clj-macro.grammar") :output-format :enlive))

(def macro-parse (insta/parser (slurp "macro.grammar") :output-format :enlive))

(def marm-str (slurp "../marmalade.md"))

(def parsed-marmalade (marm marm-str))

(def marm-arach (arachne-parse marm-str))

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

#_(map (partial re-parse clj-mac) (tag-stripper :code ex-str) ) ; works

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

(defn arach-link-strip
  "strips links on behalf of Arachne"
  [tree]
  (tag-stripper :link (re-parse link-hunter tree :prose)))
