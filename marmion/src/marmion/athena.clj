(ns marmion.athena
  (:require [instaparse.core :as insta]
            [marmion.util :refer :all]))


(def ^:private athena-parse (insta/parser (slurp "athena.grammar")
                                :output-format :enlive
                                :total true))

(defn- athena-open
  "open and parse Athena files. Returns a map with filenames as keys and
trees as values."
  [source-dir]
  (reduce merge
          (map #(assoc {} (nth % 0) (athena-parse (apply str  (nth % 1))))
               (reduce merge (slurp-files source-dir :athena)))))

(defn- spit-files
  "takes a map and spits the values into a file created from the keys"
  [athena-map source-dir destiny]
  (let [target-map (make-destinations athena-map source-dir destiny)]
    (map #(spit (path-to-string (nth % 0)) (nth % 1)) target-map)))

(defn athena
  "given source and destination directories, loads the former, applies Athena magic, then saves the files. to the latter."
  [source destiny]
  (let [athena-map (athena-open source)]
    (do
      (create-target-dirs athena-map source destiny)
      (-> athena-map
          file-map-to-string
          (spit-files source destiny)))))
