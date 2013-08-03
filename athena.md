#Athena

Athena is our weaver. This is her source file. 

As we are writing a weaver, it happens that we do not have one. This file must perforce be hand woven until Athena may take over. Asking a Goddess to take over any process should, and shall not, be done casually. 

Athena is written in [Git Flavored Markdown](https://help.github.com/articles/github-flavored-markdown), a format designed around code sharing. The executable parts are written in [Clojure](http://clojure.org), using the [Instaparse](https://github.com/Engelberg/instaparse) parsing library. 

##Bootstrap

To bootstrap Athena, we write a restricted program. It does not weave, so much as extract and concatenate code.

We then write more Markdown that specifies a macro format, also in this restricted format. We use our first weaver to weave both generations of the project into Athena, which will then be more broadly useful. 

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

What we're doing next is adding Instaparse to our project. To do this, we have to tell Leiningen to grab Instaparse, which we must do from the config file. This is normally found at `~/.lein/profiles.clj`; if it's not, I hope you know what you're doing. We add the string `[instaparse "1.2.2"]` to the `:plugins` vector.

That done, start or restart your project in Catnip, [Emacs](http://emacs.org), or however you like to do it. You must launch with `lein`, which is totally conventional.

This being a bootstrap, we will need to resort to some custom syntax in our Markdown. As we extract the source, we will encounter various `@magic words@`, which the parser will do various things with. The one in this paragraph, for example, it will ignore. The recognition sequence is `` `@ `` to begin a magic word, and `` @` `` to end one. 

These aren't macros. As you can see, they remain in the source code, and don't modify it.

Adding `[instaparse "1.2.2"]` to our project.clj gives us this:

`@/marmion/athena/project.clj@` --> where we find it, natch

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

Lein provides us with the following template in `@/marmion/athena/src/athena/core.clj@`:

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

Leiningen provides us with a test, `@/marmion/athena/src/athena/core_test.clj@"`. It begins life looking like this:

```clojure
(ns athena.core-test
  (:use clojure.test
        athena.core))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))
```

We will leave it alone for now. Eventually, we will want to test our quine against the code as it was written. 

For the same reason, we will leave the function `foo` in the namespace. Nothing will be deleted or modified, and the order in which code is introduced is the order into which it will be woven. This is a bootstrap, after all. 

Instaparse has its own format, which could be specified as a string within the .clj file. We prefer to put the grammar in its own file, `@/marmion/athena/athena.grammar@"`, which we start like this:

```
(* A Grammar for Athena, A Literate Weaver *)
```

When we complete it, we'll use Clojure's charmingly named `slurp` function to pull it into memory as a string:

`@/marmion/athena/src/athena/core.clj@`
```clojure

(def zeus-weaver (slurp "athena.grammar"))

```
zeus is, of course, that from which Athena will spring full-born. 

Our first rule is top level. The markdown may be separated into that which is kept, that which is ignored, and that which is magic.

In Instaparse, that looks something like this:

`@/marmion/athena/athena.grammar@"`
```
zeus-program = markdown code (markdown | code | magic) * ;
```

Which says a zeus program is markdown, followed by code, followed by markdown, code, or a magic word.

We'll define code next:

```
code = \n <"```"> code-type* code-block \n <```> ;
```

Which will suffice to capture our quine, with thoughtful definition of code-type and code-block.

We also need magic:

```






