(ns marmion.arachne
  (:require [instaparse.core :as insta]
            [marmion.util :refer :all]))

(def arachne-parse (insta/parser (slurp "arachne.grammar") :output-format :enlive))

(def link-hunter (insta/parser (slurp "link-hunter.grammar") :output-format :enlive))

(defn arach-link-strip
  "strips links on behalf of Arachne"
  [tree]
  (tag-stripper :link (re-parse link-hunter tree :prose)))

(def clj-mac (insta/parser (slurp "clj-macro.grammar") :output-format :enlive))

(def macro-parse (insta/parser (slurp "macro.grammar") :output-format :enlive))

(defn clj-parse
  "clojure macro parses appropriate code."
  [tree]
  (if (= (code-type tree) "clojure")
    (re-parse clj-mac tree :code-body)
     tree))

(defn tangle
  "Main Arachne function. Takes a file string, tangling all code she finds."
  [top-file]
  (let [master-parse (arachne-parse (slurp top-file))
        master-links (tag-stripper :local-file
                                   (re-parse link-hunter master-parse :prose))]
    master-links))
