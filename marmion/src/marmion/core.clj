(ns marmion.core
  (:require [instaparse.core :as insta]
            [marmion.arachne :refer :all]
            [marmion.util :refer :all]))

(def marm (insta/parser (slurp "marmalade.grammar") :output-format :enlive))



(def edn (insta/parser (slurp "edn.grammar") :output-format :enlive))

(def edn-hic (insta/parser (slurp "edn.grammar")))

(def ex-str (marm (slurp "../exmp.md")))

(def clj-mac (insta/parser (slurp "clj-macro.grammar") :output-format :enlive))

(def macro-parse (insta/parser (slurp "macro.grammar") :output-format :enlive))

(def marm-str (slurp "../marmalade.md"))

(def parsed-marmalade (marm marm-str))

(def marm-arach (arachne-parse marm-str))

#_(map (partial re-parse clj-mac) (tag-stripper :code ex-str) ) ; works

(def marm-codes (map (partial re-parse clj-mac) (tag-stripper :code parsed-marmalade)))
                                        ;breaks in the second code block

(def m-codes (tag-stripper :code marm-arach))


(defn clj-parse
  "clojure macro parses appropriate code."
  [tree]
  (if (= (code-type tree) "clojure")
    (re-parse clj-mac tree :code-body)
     tree))
