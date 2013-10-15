(ns marmion.athena
  (:require [instaparse.core :as insta]
            [clojure.string :as s]
            [me.raynes.fs :as fs]
            [marmion.util :refer :all]))


(def athena-parse (insta/parser (slurp "athena.grammar")
                                :output-format :enlive
                                :total true))

(defn athena-open
  "open and parse Athena files. Returns a map with filenames as keys and
trees as values."
  [source-dir]
  (reduce merge
          (map #(assoc {} (nth % 0) (athena-parse (apply str  (nth % 1))))
               (reduce merge (slurp-files source-dir :athena)))))

(defn make-destinations
  "make the athena map keys point to a destination directory"
  [athena-map source-dir destiny]
  (reduce merge
          (map #(assoc {}
                   (s/replace (nth % 0) source-dir destiny)
                   (nth % 1))
               athena-map)))



(defn athena-back-to-string
  "takes an athena map and turns the tree values into strings"
  [athena-map]
  (reduce merge
          (map #(assoc {} (nth % 0) (flat-tree (nth % 1)))
               athena-map)))


(defn athena-gen-dirs
  "create destination directories, if necessary."
  [athena-map source-dir destiny]
  (let [destiny-map (make-destinations athena-map source-dir destiny)
        directs (map #(fs/parent %) (keys destiny-map))
        target-dirs (sort (fn [x y]
                           (< (count (path-to-string x))
                              (count (path-to-string y)))) directs)]
    (dorun (map #(if (not (fs/directory? %)) (fs/mkdir %)) target-dirs))))

(defn athena-spit-files
  "takes a map and spits the values into a file created from the keys"
  [athena-map source-dir destiny]
  (let [target-map (make-destinations athena-map source-dir destiny)]
    (map #(spit (path-to-string (nth % 0)) (nth % 1)) target-map)))

(defn athena
  "given source and destination directories, athenifies the files."
  [source destiny]
  (let [athena-map (athena-open source)]
    (do
      (athena-gen-dirs athena-map source destiny)
      (-> athena-map
          athena-back-to-string
          (athena-spit-files source destiny)))))
