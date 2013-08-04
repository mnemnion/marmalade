(ns athena.core
    (:require [instaparse.core :as insta]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
           
(def zeus-parser (insta/parser (slurp "zeus.grammar") ))

(def parsed-athena (zeus-parser (slurp "athena.md")))
                                       
(defn cat-code "a bit of help for code blocks"
  [tag & body] (vec [tag (apply str body)]))

(def flat-athena (drop 10 (flatten (insta/transform {:code cat-code} parsed-athena))))
