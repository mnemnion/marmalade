(ns marmion.core
  (:require [instaparse.core :as insta]
            [marmion.arachne :refer :all]
            [marmion.util :refer :all]))

(def marm (insta/parser (slurp "marmalade.grammar") :output-format :enlive))

(def edn (insta/parser (slurp "edn.grammar") :output-format :enlive))

(def ex-str (marm (slurp "../exmp.md")))

(def marm-str (slurp "../marmalade.md"))

(def parsed-marmalade (marm marm-str))

(def marm-arach (arachne-parse marm-str))

(def m-codes (tag-stripper :code marm-arach))
