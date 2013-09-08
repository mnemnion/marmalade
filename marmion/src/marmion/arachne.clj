(ns marmion.arachne
  (:require [instaparse.core :as insta]
            [marmion.util :refer :all]))

(def arachne-parse (insta/parser (slurp "arachne.grammar") :output-format :enlive))

(def link-hunter (insta/parser (slurp "link-hunter.grammar") :output-format :enlive))

(defn arach-link-strip
  "strips links on behalf of Arachne"
  [tree]
  (tag-stripper :link (re-parse link-hunter tree :prose)))
