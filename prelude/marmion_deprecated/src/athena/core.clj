
(ns athena.core
    (:require [instaparse.core :as insta]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(def marmalade-parser (insta/parser (slurp "marmalade.grammar")))

(def parsed-migraine (marmalade-parser (slurp "migraine.md")))

(defn key-maker
  "makes a keyword name from our file string"
  [file-name]
  (keyword  (last (clojure.string/split file-name #"/"))))

(defn smush
  "smushes a keyword corresponding to a terminal node"
  [rule tree]
    (insta/transform 
         {rule (fn [ & lines ] [rule (apply str lines)])} 
                   tree))

(defn weave-zeus
  "a weaver to generate our next iteration"
  [state code]
  (if (keyword? (first code))
      (if (= :magic-word (first code))
          (weave-zeus (assoc state 
                             :current-file, (first (rest code))) 
                      (drop 2 code))
          (let [file-key (key-maker (:current-file state))]
              (weave-zeus (assoc state
                                 file-key, 
                                 (apply str (state file-key) (first (rest (rest code))))) 
                          (drop 3 code))))                                                      
      state))

      
#_(def zeus-map (weave-zeus {} flat-athena))
                          
#_(do (spit "migraine/zeus.grammar"  (:zeus-grammar zeus-map))
    (spit "migraine/core-test.clj" (:core_test-clj zeus-map))
    (spit "migraine/core.clj"      (:core-clj zeus-map))) 