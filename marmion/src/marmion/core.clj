(ns marmion.core
  (:require [instaparse.core :as insta]
            [me.raynes.fs :as fs]
            [marmion.athena :refer :all]
            [marmion.arachne :refer :all]
            [marmion.util :refer :all]))

(def source-files (map arachnify (slurp-files "../source/")))

(def edn-parse (total-parse (slurp "edn.grammar")))

(def ara-new (total-parse (slurp "new-arachne.grammar")))

(def toy-files (map arachnify (slurp-files "../toy/")))

(def m-codes (code-parse source-files))

(def t-codes (code-parse toy-files))

(def t-source-map (map-sources t-codes))

(def t-file-map (map-files t-codes))


(def quarry (map arachnify (slurp-files "../toy")))

(def tangles (code-parse quarry));

(def sources (map-sources tangles))

(def file-containers (map-files tangles))

(def files (expand-all-sources sources file-containers))
