(ns marmion.core
  (:require [instaparse.core :as insta]
            [me.raynes.fs :as fs]
            [marmion.athena :refer :all]
            [marmion.arachne :refer :all]
            [marmion.util :refer :all]))

(def source-files (map arachnify (slurp-files "../source/")))

(def edn-parse (insta/parser (slurp "edn.grammar") :output-format :enlive
                             :total true))
(def toy-files (map arachnify (slurp-files "../toy/")))

(def m-codes (code-parse source-files))

(def t-codes (code-parse toy-files))

(def t-source-map (map-sources t-codes))

(def t-file-map (map-files t-codes))


(defn transform-if-equal
  "takes a source string, an anchor string, and an expansion string.
if source and anchor are equal, return a map {:tag :expanded :content 'expansion'.
otherwise, return the anchor string."
  [source-string anchor expansion]
  (if (= source-string anchor)
    (assoc {:tag :expanded} :content expansion)
    (assoc {:tag :marmalade/error} :content  anchor)))


(defn expand-anchor-if-equal
  [source-string tangle expansion]

  (insta/transform {:anchor
                    (fn [& chars] (transform-if-equal source-string
                                                     (first chars)
                                                     expansion))}
                   tangle))

(defn get-expansion
  "takes a :source tangle and returns the code-body as a string"
  [tangle]
  (flat-tree (tag-stripper :code-body tangle)))

(defn get-source-macro
  "takes a :source tangle and returns the macro as a string"
  [tangle]
  (first (:content (first (tag-stripper :source tangle)))))

(defn expand-source
  "takes a :source tangle and maps it across a file-map. expands into anchors,
returning file-map."
  [source file-map]
  (map #(assoc {}
          (nth % 0)
          (expand-anchor-if-equal
           (get-source-macro source)
           (nth % 1)
           (get-expansion source)))
       file-map))
