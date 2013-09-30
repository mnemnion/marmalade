(ns marmion.arachne
  (:require [instaparse.core :as insta]
            [marmion.util :refer :all]))

(def arachne-parse (insta/parser (slurp "arachne.grammar") :output-format :enlive))

(def link-hunter (insta/parser (slurp "link-hunter.grammar") :output-format :enlive))

(def clj-mac (insta/parser (slurp "clj-macro.grammar") :output-format :enlive))

(def macro-parse (insta/parser (slurp "macro.grammar") :output-format :enlive))

(defn link-strip
  "strips links on behalf of Arachne"
  [tree]
  (tag-stripper :file (re-parse link-hunter tree :prose)))

(defn parse-macros
  "parse macros in-place on a single tree"
  [tree]
  (re-parse macro-parse tree :macro))

(defn clj-parse
  "clojure macro parses appropriate code."
  [tree]
  (if (= (code-type tree) "clojure")
    (re-parse clj-mac tree :code-body)
    (if (= (code-type tree) "grammar")
      (re-parse clj-mac tree :code-body)
      tree)))

(defn fix-final-line
  "fix the final line of a Marmalade file if necessary"
  [tree]
  (insta/transform
   {:final-line (fn [& chars] (assoc {:tag :prose} :content (list (apply str chars))))} tree))


(defn load-and-parse
  "Main Arachne fn"
  ([file]
      (-> file
          slurp
          arachne-parse
          fix-final-line))
  ([prefix file]
     (let [full-file (str prefix file)]
       (-> full-file
           slurp
           arachne-parse
           fix-final-line)
       )))

(defn load-children
  "loads and parses all child links from an Arachne tree."
  ([tree]
     (map
      #(load-and-parse (flat-tree (tag-stripper :file %)))
       (link-strip tree)))
  ([prefix tree]
    (map
      #(load-and-parse prefix (flat-tree (tag-stripper :file %)))
       (link-strip tree))))

;; (map parse-macros (map clj-parse m-codes))
;; this returns a list of all codes, with the clojure parsed for macros.

(defn strip-codes
  "takes an Arachne parse tree and returns a list of all code blocks, with macros (in Clojure blocks) parsed."
  [tree]
  ;;note that this lacks a certain generality
  (map parse-macros (map clj-parse (tag-stripper :code tree))))

(defn get-directory
  "expects a :file tree. returns a string consisting of the directory (prefix)
of the linked file"
  [file-tree]
  (apply str
         (:content (first (tag-stripper :directory file-tree)))))

(defn get-filename
  "expects a :file tree. returns a string consisting of the file name."
  [file-tree]
  (:content (first (tag-stripper :local-file file-tree))))
