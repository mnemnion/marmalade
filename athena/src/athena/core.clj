(ns athena.core
    (:require [instaparse.core :as insta]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
           
(def zp (insta/parser (slurp "athena.grammar")))

(def t-string (slurp "test/athena/test.md"))
                     
(def d-string (slurp "dirty.md"))                     
                     
(def ugly-punct (str "':/*-#[]();,-_\"`"))

(def athene (slurp "athene.md"))