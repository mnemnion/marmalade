(ns marmion.util
  (:require [instaparse.core :as insta]
            [clojure.string :as s]
            [me.raynes.fs :as fs]))


(defn key-maker
  "makes a keyword name from our file string"
  [file-name]
  (keyword  (last (clojure.string/split file-name #"/"))))

(defn smush
  "smushes a keyword corresponding to a terminal node"
  [rule tree]
    (insta/transform
     {rule (fn
             [ & lines ]
             (assoc {} :content (list (apply str lines)),
                       :tag rule))}
     tree))

;;; one for the toolbox (not used here)
(defn seq-string
  "make a string seqable by character"
  [split-me]
  (seq (drop 1 (clojure.string/split split-me #""))))

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
    (insta/transform {rule (fn [& node]
                             (re-parse parser [rule node]))} tree)
    (insta/transform {rule (fn [& node]
                             (re-parse parser {:tag rule, :content node}))} tree))) )

(defn code-type
  "expects a :code tree. Returns the type of the code,
as a string, or \\n if not found."
  [tree]
  (->  (filter #(= (:tag %) :code-header) (:content tree))
       (first)
       (:content)
       (first)))

(defn tag-stripper
  "strips :tag from tree"
  [tag parse-tree]
  (let [seq-tree (e-tree-seq parse-tree)]
    (filter #(= tag (:tag %))
            seq-tree)))


;;; File Handling


(def ^:private extensions #{ ".md" ".markdown" ".marm"})

(defn path-to-string
  "takes a path and makes it into a string"
  ([path]
     (->> path
          ;fs/normalized-path
          fs/split
          (interpose "/")
          (drop 1)
          (apply str)))
  ([path filename]
     (apply str (path-to-string path) "/" filename)))

(defn open-path
  "opens the weirdass [root dirs files] files with Marmalade-compatible
extensions."
  [root dirs files]
  (map #(if (contains? extensions (fs/extension %))
          (slurp (fs/file root %))) files))

(defn open-path-athena
  "opens [root dirs files] and produces a list of maps. keys are fully qualified
path name, value is the file as a string,"
  [root dirs files]
  (map #(if (contains? extensions (fs/extension %))
          (assoc {}
            (path-to-string root %)
            (open-path root dirs (list %))))
       files))


(defn compact
  "removes all emptyness from a seq"
  [siq]
  (filter #(not (empty? %)) siq))

(defn slurp-files
  "slurps all .md, .markdown and .marm files in [directory] and subtrees."
  ([path]
      (if (fs/directory? path)
        (compact (flatten (fs/walk open-path path)))
        (print "Error: " path "is not a directory" \newline)))
  ([path key]
     (if (= key :athena)
       (if (fs/directory? path)
         (compact (flatten (fs/walk open-path-athena path))))
       (println "Invalid Key:" key))))

(defn make-destinations
  "make the athena map keys point to a destination directory"
  [athena-map source-dir destiny]
  (reduce merge
          (map #(assoc {}
                   (s/replace (nth % 0) source-dir destiny)
                   (nth % 1))
               athena-map)))

(defn file-map-to-string
  "takes an athena map and turns the tree values into strings"
  [athena-map]
  (reduce merge
          (map #(assoc {} (nth % 0) (flat-tree (nth % 1)))
               athena-map)))

(defn create-target-dirs
  "create destination directories, if necessary."
  [athena-map source-dir destiny]
  (let [destiny-map (make-destinations athena-map source-dir destiny)
        directs (map #(fs/parent %) (keys destiny-map))
        target-dirs (sort (fn [x y]
                           (< (count (path-to-string x))
                              (count (path-to-string y)))) directs)]
    (dorun (map #(if (not (fs/directory? %)) (fs/mkdir %)) target-dirs))))

(defn containing-tag
  "takes a tree and a :tag. Returns the tree if the :tag is in it, otherwise nil"
  [rule tree]
  (if (empty? (tag-stripper rule tree))
    nil
    tree))
