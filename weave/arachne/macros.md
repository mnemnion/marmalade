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

File and source macros are header macros, and will look something like this: ```` ```clojure file:src/foo/core.clj ```` and ````clojure source:like this```. I'm split on allowing whitespace in macros, but don't see the harm. I may want to normalize the whitespace if I do. 

The rule is, currently, one block per file, and one block per source. I will relax that for files, and not for sources, in exactly one way: if a block says ```` ```clojure file:src/somefile.clj ````, and the next block says ```` ```clojure~ ```` (note the `~`), that block is also part of the same file. 

Will I allow the interleaving of multiple languages? Maybe. I can see the use case but don't want to make this harder to reason about. 

I can think of some ways to expand this that might be nice. Some anchors might resolve to a single line or symbol and it could be convenient to embed several of those in a code block. If so, they will have a distinct "container" format. A code block must have either free code or any number of containers, but not both. I won't add this unless I'm feeling pain. 

There are both developer and user justifications for this. As a developer, it happens that it's easy to make decisions about the code body from the code header. It's already packed up into a nice tree.

This conceptual clarity will help the user also. When we develop Athena further, it will be easy to turn our file macros into links into the tangle that provide the created file, and mark up the file name attractively as a header for the code block. Similarly, the source names can be converted into legible headers, and the anchors turned into links into the source code block. 

Also, code is the land and Markdown is the ocean. Treating the blocks as mostly disconnected and atomic units will aid clarity, encourage the use of macros, and make the resulting weave easier to read.

Most importantly, this make writing Marmion difficult, not fiendishly difficult with numerous failure modes. This reduces merge conflicts to one case: edits to a macro source, in the tangle, where that macro is expanded in more than one place. This kind of thing is uncommon.

Marmalade macros are meta-macros. If you want to do fancy macro stuff, use a language that supports it. A major justification for writing Marmalade is polyglot codebases, so we can assemble working systems using the best tools for the job. That's what we do anyway, and the Marmalade toolchain systematizes it. 

##Marmalade Proper

By adding header macros, we have officially made a dialect of GFM. Marmalade files do not parse as Markdown, causing me to take a day to write the first true Athena. She simply takes Marmalade code, strips the header macros, and emits compatible Markdown. Eventually, she can do a great deal more than that. But the circuit is complete; we may make breaking changes to our Markdown documents, generate the weave, and have readable Markdown which GitHub helpfully autogens into HTML. 
