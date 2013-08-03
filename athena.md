#Athena

Athena is our weaver. This is her source file. 

As we are writing a weaver, it happens that we do not have one. This file must perforce be hand woven until Athena may take over. Asking a Goddess to take over any process should, and shall not, be done casually. 

Athena is written in [Git Flavored Markdown](https://help.github.com/articles/github-flavored-markdown), a format designed around code sharing. The executable parts are written in [Clojure](http://clojure.org), using the [Instaparse](https://github.com/Engelberg/instaparse) parsing library. 

Clojure projects are typically generated with [Leiningen](https://github.com/technomancy/leiningen), and Athena is no exception. Leiningen projects are specified in a root directory file called `project.clj`. 

This is project.clj:

```clojure
(defproject athena "0.1.0-SNAPSHOT"
  :description "Athena: a Weaver of Code"
  :url "http://github.com/mnemnion/marmion/athena"
  :license {:name "BSD 2-Clause License"
            :url "http://http://opensource.org/licenses/BSD-2-Clause"}
  :dependencies [[org.clojure/clojure "1.4.0"]])
```

