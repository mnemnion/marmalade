(ns athena.core
    (:require [instaparse.core :as insta]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
           
(def zp (insta/parser (slurp "zeus.grammar")))

(def t-string (slurp "test/athena/test.md"))
                     
(def d-string (slurp "dirty.md"))                     
                     
(def ugly-punct (str "':/*-#[]();,-_\"`"))

(def athene (slurp "athene.md"))
                   
(def a-fr (apply str (drop 000 (take 3500 athene))))
                   
(def toy (insta/parser " parsi = fubar / fubar baz fubar = #'foobar|foo' baz = #'[A-Za-z]+' " ))