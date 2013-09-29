(ns marmion.core
  (:require [instaparse.core :as insta]
            [marmion.arachne :refer :all]
            [marmion.util :refer :all]))

(def marm (insta/parser (slurp "marmalade.grammar") :output-format :enlive))

(def edn (insta/parser (slurp "edn.grammar") :output-format :enlive))

(def ex-str (marm (slurp "../exmp.md")))

(def marm-str (slurp "../source/marmalade.md"))

(def parsed-marmalade (marm marm-str))

(def marm-arach (load-and-parse "../source/" "marmalade.md"))

(def m-codes (strip-codes marm-arach))

(def marm-links (link-strip marm-arach))

(def ara-ath
  (load-children "../source/" marm-arach))

(def code-base (into m-codes (map strip-codes ara-ath)))
