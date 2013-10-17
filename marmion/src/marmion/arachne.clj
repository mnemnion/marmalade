(ns marmion.arachne
  (:require [instaparse.core :as insta]
            [me.raynes.fs :as fs]
            [marmion.util :refer :all]))

(def ^:private arachne-parse
  (insta/parser (slurp "arachne.grammar") :output-format :enlive))

(def header-parser (insta/parser (slurp "header-macros.grammar")
                               :output-format :enlive))

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
  (re-parse macro-parser tree :macro ))

(defn header-parse-tree
  "parses header macros on a :tangle block"
  [tree]
  (re-parse header-parser tree :header-macro))


(defn clj-parse
  "clojure macro parses appropriate code."
  [tree]
  (re-parse clj-mac tree :code-body))

(defn strip-codes
  "takes an Arachne parse tree and returns a list of all code blocks, with macros (in Clojure blocks) parsed."
  [tree]
  ;;note that this lacks a certain generality
  (map macro-parse-tree (map clj-parse (tag-stripper :tangle tree))))

(defn code-parse
  "Strips codes from arachne-parsed trees and macro-parses them"
  [md-tree-list]
  (->> md-tree-list
       (map strip-codes)
       flatten
       (map macro-parse-tree)
       (map header-parse-tree)
       (map #(smush :literal-code %))))

(defn- make-rule-map
  "takes a :tangle-map and :rule and produces a map with the :rule string as the key and the :code-body as the val"
  [tangle rule]
  (if (containing-tag rule tangle)
    (assoc {} (first (:content (first (tag-stripper rule tangle)))) (tag-stripper :code-body tangle))
    nil))

(defn make-file-map
  "makes a file-string to :code-body map from a :tangle"
  [tangle]
  (make-rule-map tangle :file))

(defn make-source-map
  "makes an anchro-macro string to :code-body map from a :tangle"
  [tangle]
  (make-rule-map tangle :source))

(defn- map-rules
  "takes a list of :tangles and a function and returns a map."
  [tangle-list rule-map-fn]
  (reduce merge
          (filter #(not (nil? %)) (map #(rule-map-fn %) tangle-list))))

(defn map-files
  "takes a list of :tangles, returns a map of :file tangles to associated :code-body"
  [tangle-list]
  (map-rules tangle-list make-file-map))

(defn map-sources
  "takes a list of tangles, returns a map of :source tangles to associated :code-body"
  [tangle-list]
  (map-rules tangle-list make-source-map))



(defn- transform-if-equal
  "takes a source string, an anchor string, and an expansion string.
if source and anchor are equal, return a map {:tag :expanded :content 'expansion'.
otherwise, return the anchor string."
  [source-string anchor expansion]
  (if (= source-string anchor)
    (assoc {:tag :expanded} :content expansion)
    (assoc {:tag :marmalade/error} :content  anchor)))


(defn- expand-anchor-if-equal
  [source-string tangle expansion]

  (insta/transform {:anchor
                    (fn [& chars] (transform-if-equal source-string
                                                     (first chars)
                                                     expansion))}
                   tangle))

(defn- get-expansion
  "takes a :source tangle and returns the code-body as a string"
  [tangle]
  (flat-tree (tag-stripper :code-body tangle)))

(defn- get-source-macro
  "takes a :source tangle and returns the macro as a string"
  [tangle]
  (first (:content (first (tag-stripper :source tangle)))))

(defn- expand-source
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

(defn expand-all-sources
  "maps the source map against the file map. Expands."
  [source-map file-map]
  (map #(expand-source (nth % 1) file-map) source-map))

(defn arachne-create-target-dirs
  "and here we go loopty-loo"
  [arachne-map]
  (let [directs  (map #(fs/parent  %) (keys arachne-map))
        target-dirs (sort (fn [x y]
                           (< (count (path-to-string x))
                              (count (path-to-string y)))) directs)]
    (dorun (map #(if (not (fs/directory?  %))
                     (fs/mkdir %)) target-dirs))
    directs))




#_(defn arachne
  [source destiny]
  (let [arachne-files (map arachnify (slurp-files source))
        tangles (code-parse arachne-files)
        sources (map-sources tangles)
        file-containers   (map-files tangles)
        files (first (expand-all-sources sources file-containers))]
    #_(-> files
        (create-target-dirs source destiny)
        file-map-to-string
        (spit-files source destiny)))
  files)
