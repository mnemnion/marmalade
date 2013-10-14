(ns marmion.core
  (:require [instaparse.core :as insta]
            [me.raynes.fs :as fs]
            [marmion.arachne :refer :all]
            [marmion.util :refer :all]))

(def marm-arach (load-and-parse "../source/" "marmalade.md"))

(def source-files (map arachnify (slurp-files "../source/")))

(def toy-files (map arachnify (slurp-files "../toy/")))

(def m-codes (code-parse source-files))
