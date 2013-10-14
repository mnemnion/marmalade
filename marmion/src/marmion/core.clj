(ns marmion.core
  (:require [instaparse.core :as insta]
            [me.raynes.fs :as fs]
            [marmion.arachne :refer :all]
            [marmion.util :refer :all]
            [marmion.links :refer :all]))

(def marm (insta/parser (slurp "marmalade.grammar") :output-format :enlive))

(def edn (insta/parser (slurp "edn.grammar") :output-format :enlive))

(def ex-str (marm (slurp "../exmp.md")))

(def marm-str (slurp "../source/marmalade.md"))

(def parsed-marmalade (marm marm-str))

(def marm-arach (load-and-parse "../source/" "marmalade.md"))

(def m-codes (strip-codes marm-arach))

(def marm-links (link-strip (get marm-arach "../source/marmalade.md")))

(def ara-ath
  (load-children "../source/" (get marm-arach "../source/marmalade.md")))

(def code-base (into m-codes (map strip-codes ara-ath)))

(def ath (load-and-parse "../source/" "athena/athena.md"))

(def ara (load-and-parse "../source/" "arachne/arachne.md"))

(def source-files (map arachnify (slurp-files "../source/")))
