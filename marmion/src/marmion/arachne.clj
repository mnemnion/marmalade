(ns marmion.arachne
  (:require [instaparse.core :as insta]
            [marmion.util :refer :all]))

(def arachne-parse (insta/parser (slurp "arachne.grammar") :output-format :enlive))

(def link-hunter (insta/parser (slurp "link-hunter.grammar") :output-format :enlive))

(def tangle-box {})

(defn link-strip
  "strips links on behalf of Arachne"
  [tree]
  (tag-stripper :local-file (re-parse link-hunter tree :prose)))

(def clj-mac (insta/parser (slurp "clj-macro.grammar") :output-format :enlive))

(def macro-parse (insta/parser (slurp "macro.grammar") :output-format :enlive))

(defn clj-parse
  "clojure macro parses appropriate code."
  [tree]
  (if (= (code-type tree) "clojure")
    (re-parse clj-mac tree :code-body)
    tree))

(defn fix-final-line
  "fix the final line of a Marmalade file if necessary"
  [tree]
  (insta/transform
   {:final-line (fn [& chars] (assoc {:tag :prose} :content (list (apply str chars))))} tree))

#_(defn tangle ;too soon
  "Main Arachne function. Takes a file string, tangling all code she finds."
  [prefix top-file]
  (let [master-parse (arachne-parse (slurp (str prefix top-file)))
        master-links (arach-link-strip master-parse)]
    (assoc tangle-box :files (list top-file))
    (assoc tangle-box :files (into (list top-file)
                                   (filter #(slurp (str prefix (first (:content %))))
                                           master-links)))))


(defn load-and-parse
  "Main Arachne fn"
  ([file]
      (-> file
          (slurp)
          (arachne-parse)
          (fix-final-line)))
  ([prefix file]
     (let [full-file (str prefix file)]
       (-> full-file
           (slurp)
           (arachne-parse)
           (fix-final-line))
       )))
