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

(defn key-maker
  "makes a keyword name from our file string"
  [file-name]
  (keyword (clojure.string/replace (last (clojure.string/split file-name #"/")) "." "-")))

(defn weave-zeus
  "a weaver to generate our next iteration"
  [state code]
  (if (keyword? (first code))
      (if (= :magic-word (first code))
          (weave-zeus (assoc state 
                             :current-file, (first (rest code))) 
                      (drop 2 code))
          (if (= :code-type (first code))
              
              (let [file-key (key-maker (:current-file state))]
              (weave-zeus (assoc state
                                 file-key, 
                                 (apply str (state file-key) (first (rest (rest code))))) 
                          (drop 3 code)))
              (println "error")))                                                      
      state))
     
(defn huh
  "huh?"
  [_]
  (if (= 0 0)
      ((println "many things can happen under such conditions")
       +(println "why, almost anything might be true")
        (+ 2 2))
      (println "this never happens of course")))
               
               