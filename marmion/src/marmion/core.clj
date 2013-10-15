(ns marmion.core
  (:require [instaparse.core :as insta]
            [me.raynes.fs :as fs]
            [marmion.arachne :refer :all]
            [marmion.util :refer :all]))

(def source-files (map arachnify (slurp-files "../source/")))

(def toy-files (map arachnify (slurp-files "../toy/")))

(def m-codes (code-parse source-files))

(def t-codes (code-parse toy-files))

;(def compact-codes (map #(smush :literal-code %) m-codes))
