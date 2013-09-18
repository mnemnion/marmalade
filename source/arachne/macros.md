#Literate Macros

The term macro generally refers to a function that rewrites code prior to any attempt to make sense of it. Whether Coffeescript is a macro preprocessor over Javascript or a language in its own right is a matter of taste. Macros can get arbitrarily complex. We will keep ours as simple as practical.

Our simplest type of macro is an anchor. It is surrounded with a triglyph on both sides, e.g. ` ~<$macro$>~ `, where the triglyph selected is one that will not appear in either order in the target language.

Second rule: if the triglyphs are `~<$` and `$>~`, then `$><$` must also be an invalid token in the targeted language. We will use this rule for chaining macros. Those are also the default triglyphs in code blocks, if none are provided.

We will provide a mechanism for defining macro glyphs in the ```` ```config ```` blocks, later. For now we're hard wiring them. Marmalade has one, which I won't type until the code is sanitized to allow literal quoting of the macro form. The Clojure macros are `#|(` and `)|#` with the tetraglyph form `)||(`. They are syntax errors and likely to stay so. If it turns into anything legitimate, it will be a block comment a la Common Lisp. Rich has declared his opposition.

To make life simple, we'll reuse this for Instaparse grammar files also. It's decidedly gibberish in that language also.

It's quite possible to write a legit Marmalade file that doesn't use any Markdown macros. Without macros in the code blocks, there will be no tangle. The default behavior around a code block with no macros is to include it in the weave but not the tangle. Often this is what we want, if we are quoting a short piece of code that never gets used or that has been deprecated in favor of better code.

Anchor macros have an interior syntax. The simplest takes the form ` ~<$name$>~ `. The typical use is as a point in code for macro expansion. If so, Arachne will attempt to fill the macro from a code block called `~<$source:name$>~`. `source` is a keyword, not a variable.

Another keyword form is `~<$file:/src/file.extn$>~`. Any code block containing such a macro at the top will end up in that file, after all other macros are fully expanded.

There is a short continuation form used to intersperse code and commentary. Let's say we're targeting foo-lang, and we have declared a ```` ```foo-lang ```` block that has started a file. If the next foo-lang block looks like ```` ```foo-lang~ ```` with a `~` after it, that block continues in the same file.

Each language may be considered namespaced in this regard, so if you are interweaving two or more languages, each will go into the current file. Try to keep things easy to follow.

###Macro Predicates

In addition, a macro may have a prefix, which is an ordinary symbol which must end in the character `?`. In the configuration, the prefix may be set to 'true' or 'false'. If the prefix is false, the macro doesn't exist.

This is a simple `#ifdef` kind of refinement that lets use build multiple versions from a single codebase.

I am wary of adding more complexity than this.

So here's a small grammar for parsing macros, once they have been located.

```grammar
#|(file:macro.grammar)|#

(* A Micro Grammar For Macros *)

mac-name = prefix? command ':' mac-id
         | mac-id
         ;

prefix = #'[0-9A-Za-z.!+\- ]+\?'

mac-id = #'[0-9A-Za-z.!+\-_/ ]*'
         ;

command = #'[0-9A-Za-z.!+\-_ ]+'
          ;
```

Which does pretty much what you'd expect, restricting our macros to alphanumerics, `-`,`.`,`!`, and `+` for now. `?` and `:` may be found once each, must be in that order if both are present, and `?` further more joins the prefix so that the prefix is parsed as `debug?` not `debug` `?`.
