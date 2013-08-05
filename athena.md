#Athena

Athena is our weaver. This is her source file. 

As we are writing a weaver, it happens that we do not have one. This file must perforce be hand woven until Athena may take over. Asking a Goddess to take over any process should not, and shall not, be done casually. 

Athena is written in [Git Flavored Markdown](https://help.github.com/articles/github-flavored-markdown), a format designed around code sharing. The executable parts are written in [Clojure](http://clojure.org), using the [Instaparse](https://github.com/Engelberg/instaparse) parsing library. 

#Hymn

Hail, fleet footed Hermes, beloved of Athena!

Hail, Pallas Athene! Hear the ancient words:

```
I begin to sing of Pallas Athena, the glorious Goddess, bright-eyed,  
inventive, unbending of heart,  
pure virgin, saviour of cities,  
courageous, Tritogeneia. Wise Zeus himself bare her  
from his awful head, arrayed in warlike arms  
of flashing gold, and awe seized all the gods as they gazed.  
But Athena sprang quickly from the immortal head 
and stood before Zeus who holds the aegis,  
shaking a sharp spear: great Olympus began to reel horribly 
at the might of the bright-eyed Goddess, 
and earth round about cried fearfully,  
and the sea was moved and tossed with dark waves,  
while foam burst forth suddenly:  
the bright Son of Hyperion stopped his swift-footed horses a long while, 
      until the maiden Pallas Athena had stripped the heavenly armour 
      from her immortal shoulders.  
And wise Zeus was glad. 
```

And so hail to you, daughter of Zeus who holds the aegis!  
Now I will remember you and another song as well.  

##Bootstrap

To bootstrap Athena, we write a restricted program. It does not weave, so much as extract and concatenate code.

We then write more Markdown that specifies a macro format, also in this restricted format. We use our first weaver to weave both generations of the project into Athena, which will then be more broadly useful. 

This first weaver will be known as zeus. zeus is, of course, that from which Athena will spring full-born. 

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

The above code contains no macro, yet. Writing macros in a Lisp is of course pleasurable and powerful, and Clojure is no exception, having definable [reader macros](http://clojure.org/reader). Soon, we will define one.

First, however, we need a parser that can extract our code. For that, we need to add [Instaparse](https://github.com/Engelberg/instaparse).

Time to fire up [Catnip](https://github.com/bodil/catnip) real quick. Be back soon!

What we're doing next is adding Instaparse to our project. To do this, we have to tell Leiningen to grab Instaparse, which we must do from the config file. This is normally found at `~/.lein/profiles.clj`; if it's not, I hope you know what you're doing. We add the string `[instaparse "1.2.2"]` to the `:plugins` vector.

That done, start or restart your project in Catnip, [Emacs](http://emacs.org), or however you like to do it. You must launch with `lein`, which is totally conventional.

This being a bootstrap, we will need to resort to some custom syntax in our Markdown. As we extract the source, we will encounter various `@magic words@`, which the parser will do various things with. The ones in this paragraph, for example, it will ignore. The recognition sequence is `` `@ `` to begin a magic word, and `` @` `` to end one. 

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

Leiningen provides us with a test, `@/marmion/athena/src/athena/core_test.clj@`. It begins life looking like this:

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

Instaparse has its own format, which could be specified as a string within the .clj file. We prefer to put the grammar in its own file, `@/marmion/athena/zeus.grammar@`, which we start like this:

```text
(* A Grammar for Zeus, Father of Athena, A Literate Weaver *)

```

Our first rule is top level. The markdown may be separated into that which is kept, that which is ignored, and that which is magic.

In Instaparse, that looks something like this:

```text

zeus-program = (magic | code | <markdown>) * 
```

What this says is that a zeus program is any combination of magic, code, and markdown. Since Zeus does nothing with the markdown, we use angle brackets to tell Instaparse that we don't care to see the output. 

We'll define code next:

```text

code =  <"`" "`" "`"> code-type code-block+ <"`" "`" "`"> 
   | <"`" "`" "`"> "\n" code-block+ <"`" "`" "`">
   ;

code-type = "clojure" | "text" ;

<code-block> = #'[^`]+' | in-line-code ;

<in-line-code> = !("`" "`" "`") ("`"|"``");
```

Which will suffice to capture our quine. 

Please note: we could use a more direct way to capture three `` ` ``, if we weren't writing a peculiar quine. Zeus uses the simplest possible grammar to extract a minimalist weaver from this very source file. 

A couple notes: `code-glob` is mostly a regular expression that doesn't consume backticks. `in-line-code` uses negative lookahead `!`, which is great stuff: it says, if there aren't three backticks ahead of us, you may match one or two of those backticks. 

Between them, they match everything except three backticks. Real Markdown uses newlines and triple backticks together. This is harder to write and understand, so we'll do it in the second pass.

We also need magic:

```text

<magic> = <"`@"> magic-word <"@`"> ;

magic-word = #'[^@]+' ; 
```

Which is defined fairly carefully to consume our magic words. We don't use the at-sign elsewhere in the outer Markdown to enable easy magic.

That leaves `markdown` which is perhaps not strictly named, since the code blocks are markdown also. For Zeus, we may as well call it junk; we have to match it, but we don't look at it. It looks like this:

```text


markdown = #'[^`@]+' | in-line-code;
```

We're done! We now have a grammar that we can make into a parser, so let's do it: we need to add more code to `@/marmion/athena/src/athena/core.clj@`. 

```clojure

(def zeus-parser (insta/parser (slurp "zeus.grammar")))
```

That was simple enough. It disguises the toil of repeatedly writing bad, useless and exponentially explosive grammars.

But then, literature generally hides the messiness behind its production. If you have read the unedited *Stranger in a Strange Land*, which Heinlein never wanted published, you can see why. Presuming you've read the edited version. 

Now, we use `zeus-parser` to parse this document, `athena.md`

```clojure

(def parsed-athena (zeus-parser (slurp "athena.md")))
```

When we run `core.clj` in a REPL, we see that `parsed-athena` is a tree-structure containing our magic words and code. We've designed this puzzle so that we can use this sorted information in the order we found it, so we don't need the tree structure.

To simply get rid of the tree structure we would `flatten` it. But this would leave us in a bad way, because some of our code blocks aren't globbed into a single string, thanks to separate detection for `` ` ` `` [sic] and `` ` ``. 

Fortunately, this is so common that Instaparse ships with a function for fixing it. `insta/transform` to the rescue!

First we need a helper function for insta/transform to call:

```clojure

(defn cat-code "a bit of help for code blocks"
  [tag & body] (vec [tag (apply str body)]))
```

Then we call it and do some stuff to the results:

```clojure

(def flat-athena (drop 10 (flatten (insta/transform {:code cat-code} parsed-athena))))
```

Now, how you feel about this line depends on how you feel about Lisp, generally. This was written progressively from the middle out, on a REPL. It's easy to read if you know that, and would be easier still if formatted more naturally. 

A more idiomatic Clojure way to do all this would be to use a threading macro like `->>` to thread the data structure through the transformations, instead of making all these global defs. Everything so far could be a single function, though it's sensible to put the parser in its own ref.

`drop 10` just gets us past the front matter. We introduce our idioms before we use them, for a reason.

We now have a flat vector, containing all the information we need. We need to transform it into a data structure which may then be massaged and spat out as our original project and core files. 

The quine could be completed with a trivial act, which we put in the margins: `(spit athene-as-is.md (remove-tags-flatten-and-concatenate (zeus-parser (slurp athena.md) :unhide :all)))`, which calls a function we needn't bother to write. All this does is parse athena.md, remove the tags, flatten the remaining literal values, which, because we used `:unhide :all`, was everything from our original source file. Cute, but not interesting enough to belong in the quine. Opening your source file, doing nothing interesting to it, and saving/printing it is generally a trivial quine, though if the convolutions you put the text through are hard enough to follow you will amuse someone at least.

Instead, let's write a little helper function, `key-maker`

```clojure

(defn key-maker
  "makes a keyword name from our file string"
  [file-name]
  (clojure.string/replace (last (clojure.string/split file-name #"/")) "." "-"))
```

This takes our fully-qualified filename, pulled from a magic word, and keywordizes it. The magic words are arranged so there's one each time zeus needs to change files.

Now for the meat of the matter. `weave-zeus` produces the source file to zeus from the markdown version of this very file. 

```clojure
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
```

Now, that's an ugly hack. It's a bootstrap; I fiddled with it until it worked and dropped the matter thereafter. It is perhaps more readable than a more elegant version, if you have, like most of us, a background or ongoing investment in imperative style. The fact that there's a clause that one shouldn't even reach, and that we don't, should be grounds for a rewrite. We aren't touching it further, though we use it as a spring-off point for migraine, the next step in the process.

Migraine because it actually gives birth to Athena. Named in honor of whichever poor sufferer dreamed that mythos up. 

So we move the latest athena.md into the project directory, load up the REPL and sure enough, it's all in there. Now we just have to `spit` it out. To do it right, we'd have kept some exact record of our file name so we could put it into a new directory of the same form. We're going to cheat instead; we did the hard part, and want to keep it readable since we don't have macros with which to bury the boring parts.

So here's our last trick:

```clojure
      
(def zeus-map (weave-zeus {} flat-athena))
                          
(do (spit "zeus.grammar"  (:zeus-grammar zeus-map))
    (spit "core-test.clj" (:core_test-clj zeus-map))
    (spit "core.clj"      (:core-clj zeus-map))) 
```

That's it! This leaves our files stranded in the home directory, and there are some extra newlines, but I don't care and neither should you. 

Migraine, the next chapter in this adventure, will add some actual capabilities. Migraine is just Zeus with an extra headache: instead of producing himself, he has to produce Athena, which is a more challenging software to write.   

