#Arachne

Arachne is our tangler, extracting usable code from the source, arranging it, and taking whatever actions are necessary to render it executable.

The arachne file looks like this:

```clojure
#|(file:arachne.clj)|#
(ns marmion.arachne
  (:require [instaparse.core :as insta]
            [marmion.util :refer :all]))

(def arachne-parse (insta/parser (slurp "arachne.grammar") :output-format :enlive))

(def link-hunter (insta/parser (slurp "link-hunter.grammar") :output-format :enlive))

(defn link-strip
  "strips links on behalf of Arachne"
  [tree]
  (tag-stripper :local-file (re-parse link-hunter tree :prose)))

(def clj-mac (insta/parser (slurp "clj-macro.grammar") :output-format :enlive))

(def macro-parse (insta/parser (slurp "macro.grammar") :output-format :enlive))

(defn parse-macros
  "parse macros in-place on a single tree"
  [tree]
  (re-parse macro-parse tree :macro))

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
           slurp
           arachne-parse
           fix-final-line)
       )))

(defn load-children
  "loads and parses all child links from an Arachne tree."
  ([tree]
     (map #(load-and-parse (first (:content %))) (link-strip tree)))
  ([prefix tree]
    (map #(load-and-parse prefix (first (:content %))) (link-strip tree))))

;;(map parse-macros (map clj-parse m-codes))
;; this returns a list of all codes, with the clojure parsed for macros.
```

This kind of "markdown dump" shouldn't be necessary once we have the toolchain up. Bootstrapping requires lots of scrapping.
