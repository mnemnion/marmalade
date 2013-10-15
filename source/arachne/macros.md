#The Marmalade Macro System

What makes literate programming distinctive is not the interleaving of documentation and code. That's a weaker paradigm, frequently reinvented. Two variations: one is embedding documentation in comments or strings in the code, the other is embedding code in a comment syntax and extracting it. The former is Javadoc, doc strings in Python/Clojure/Common Lisp/YouNameIt; the latter is used in Literate Coffeescript, which is not true literate programming, albeit an improvement over nothing at all.

The real power of literate programming lies in separating the logic of the program from the logic of the programming language or environment. To unleash this power requires macros. 

Macros can rapidly become too much power. In particular, our literate toolchain relies on three operations: tangling, weaving, and untangling. If the macro system is too general, the source will become the only place where code may be edited. We wish to retain the ability to make changes in the tangle and have them back propagate into the source and hence the weave. That's trivial, except for macros. 

##Macros in Marmalade

Knuth's original literate system has a truly fantastical set of macros. He was working in the dark days, when both markup of documents for publishing and usable programming languages had to be invented from whole cloth.

We have a tremendous advantage with Git Flavored Markdown. I feel like it's teetering on the verge of being literate as-is. We want to make thoughtful choices when we expand it.

The first approach was straightforward: code blocks are code, and prose is everything else. Anything of use to Arachne was therefore to be found within code blocks. Very well, as far as it goes. 

I'm going to try something different, that I hope will be more powerful. Namely, making code blocks a first-class citizen in the environment. 

This will require a (hopefully quick) diversion to write some of Athena, because as of this change, Marmalade will no longer be valid GFM. There's no advantage to keeping that constraint, so I don't intend to. 

GFM code blocks have a header that looks like ```` ```language ````. We're already parsing that header (and everything else), and using that information. Let's use that for some of our macros, shall we? 

##Minimal Macros

Our bare minimum task: specify certain code blocks as files, create anchor macros to put in the code blocks, and specify certain other code blocks as the source for those macros. Anchor macros look ` #|(like this)|# ` in Clojure; we use different trigrams for different programming environments. It would be easy to get weird with it if trigrams don't suffice. 

File and source macros are header macros, and will look something like this: ```` ```clojure file:src/foo/core.clj ```` and ````clojure source:like this```. I'm split on allowing whitespace in macros, but don't see the harm. I may want to normalize that...
