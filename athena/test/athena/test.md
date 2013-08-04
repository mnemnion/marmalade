front matter http://example.com *emphasis* _subtle_ ` exact ` `@magic-word/file.txt@` post matter

#A test




This is a *test* for [Athena](http://i-needn't-work.com). 

It has code :

```
littlebits; of; code;
```


What we're doing next is adding Instaparse to our project. To do this, we have to tell Leiningen to grab Instaparse, which we must do from the config file. This is normally found at `~/.lein/profiles.clj`; if it's not, I hope you know what you're doing. We add the string `[instaparse "1.2.2"]` to the `:plugins` vector.

That done, start or restart your project in Catnip, [Emacs](http://emacs.org), or however you like to do it. You must launch with `lein`, which is totally conventional.

It has Clojure :


`@/marmion/athena/project.clj@` --> where we find it, natch

```clojure
(defproject athena "0.1.0-SNAPSHOT"
  :description "Athena: a Weaver of Code"
  :url "http://github.com/mnemnion/marmion/athena"
  :license {:name "BSD 2-Clause License"
            :url "http://http://opensource.org/licenses/BSD-2-Clause"}
  :dependencies [[org.clojure/clojure "1.4.0"]])
```

and it has magic: `@i am-made-of/magic.words@`

and more **markdown** ` don'tcha know ` . _woot_.

bye!