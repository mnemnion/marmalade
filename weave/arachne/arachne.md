#Arachne

Arachne is our tangler, extracting usable code from the source, arranging it, and taking whatever actions are necessary to render it executable.

##TODO

What we need to do:

  1. Parse the head file
  1. Parse and follow all interior links
  1. Parse the files thereby found
  1. Parse all code blocks for macros
  1. Expand said macros into file strings
  1. Spit out the file strings into the tangle.

Pretty much, that's the job. I'm curious to see what our Marmalade parser will do with that list. Should be :prose (it is).


The arachne file looks like this:

```clojure
#|(file:src/arachne.clj)|#
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

##Link Hunter

Arachne has some work to do in our :prose as well. Most links are not her business, rather Athena's. Arachne does need to look in any local links for more source code that may contain weavable code blocks.

Arachne won't look for these kinds of links in headers, only in :prose blocks. Let's write a little `re-parse` grammar to find these kinds of links.

```grammar
#|(file:link-hunter.grammar)|#
(* A Link Hunting Mini Grammar for Arachne *)

prose = categories+

<categories> = (stuff | link) / punct ;

<stuff> = #'[^\[\]()]+' ;

link = "[" #'[^\]]+' "]" WS* "(" local-file ")" ;

local-file = #"[^):]+" ;

punct = #"[\[\]()]" ;

WS = #'[ ]+' ;
```

##Subdirectories

Let's add a [relative link](sub/sub.md), shall we? Now let's make that file exist.
