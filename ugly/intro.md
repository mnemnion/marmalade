#Quick And Dirty Quine

Now that we have the elements, let's quine the software:

```clojure file: project.clj
(defproject marmalade "0.1.1-SNAPSHOT"
  :description "Marmalade: a Literate Markdown in Three Acts"
  :url "http://github.com/mnemnion/Marmalade/marmion"
  :license {:name "BSD 2-Clause License"
            :url "http://http://opensource.org/licenses/BSD-2-Clause"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [instaparse "1.2.4"]
                 [me.raynes/fs "1.4.4"]])
```

Okay, there's project.clj. Here's core:

```clojure file: src/marmion/core.clj
(ns marmion.core
  (:require [instaparse.core :as insta]
            [me.raynes.fs :as fs]
            [marmion.athena :refer :all]
            [marmion.arachne :refer :all]
            [marmion.util :refer :all]))

(def source-files (map arachnify (slurp-files "../source/")))

(def edn-parse (insta/parser (slurp "edn.grammar") :output-format :enlive
                             :total true))
(def toy-files (map arachnify (slurp-files "../toy/")))

(def m-codes (code-parse source-files))

(def t-codes (code-parse toy-files))

(def t-source-map (map-sources t-codes))

(def t-file-map (map-files t-codes))

```

Bob may at this point be our uncle. Let's find out!
