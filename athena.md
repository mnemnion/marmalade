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

In order to weave code, in general, we need a macro format. This may be made as flexible as necessary. The minimal requirement is the ability to specify a macro name, and expand those macros into files. 

This is weaving in its essence.

The above code contains no macro, yet. Writing macros in a Lisp is of course pleasurable and powerful, and clojure is no exception, having definable [reader macros](http://clojure.org/reader). Soon, we will define one.

First, however, we need a parser that can extract our code. For that, we need to add [Instaparse](https://github.com/Engelberg/instaparse).

Time to fire up [Catnip](https://github.com/bodil/catnip) real quick. Be back soon!

What we're doing next is adding Instaparse to our project. To do this, we have to tell Leiningen to grab Instaparse, which we must do from the config file. This is normally found at `~/.lein/profiles.clj`; if it's not, I hope you know what you're doing. 

That done, start or restart your project in Catnip, [Emacs](http://emacs.org), or however you like to do it. You must launch with `lein`, which is totally conventional.

Adding `[instaparse "1.2.2"]` to our project.clj gives us this:

```clojure
(defproject athena "0.1.0-SNAPSHOT"
  :description "Athena: a Weaver of Code"
  :url "http://github.com/mnemnion/marmion/athena"
  :license {:name "BSD 2-Clause License"
            :url "http://http://opensource.org/licenses/BSD-2-Clause"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [instaparse "1.2.2"]])
```

Which should compile. 

Lein provides us with the following template in `src/core.clj`:

```clojure
(ns athena.core
    (:require [instaparse.core :as insta]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
```

We added instaparse.core there. This should compile too. 

We now have a powerful and general [GLL] parser at our disposal. Yippie!

Let's do something with it!

How about we open up our source file, `athena.md`, and see if we can produce a quine of our existing file and directory structure?


