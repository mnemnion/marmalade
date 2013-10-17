#The Marmalade Macro System

What makes literate programming distinctive is not the interleaving of documentation and code. That's a weaker paradigm, frequently reinvented. Two variations: one is embedding documentation in comments or strings in the code, the other is embedding code in a comment syntax and extracting it. The former is Javadoc, doc strings in Python/Clojure/Common Lisp/YouNameIt; the latter is used in Literate Coffeescript, which is not true literate programming, albeit an improvement over nothing at all.

The real power of literate programming lies in separating the logic of the program from the logic of the programming language or environment. To unleash this power requires macros.

Macros can rapidly become too much power. In particular, our literate toolchain relies on three operations: tangling, weaving, and untangling. If the macro system is too general, the source will become the only place where code may be edited. We wish to retain the ability to make changes in the tangle and have them back propagate into the source and hence the weave. That's trivial, except for macros.

##Macros in Marmalade

Knuth's original literate system has a truly fantastical set of macros. He was working in the dark days, when both markup of documents for publishing and usable programming languages had to be invented from whole cloth.

We have a tremendous advantage with Git Flavored Markdown. I feel like it's teetering on the verge of being literate as-is. We want to make thoughtful choices when we expand it.

The first approach was straightforward: code blocks are code, and prose is everything else. Anything of use to Arachne was therefore to be found within code blocks. Very well, as far as it goes. It had a powerful advantage to begin with, namely that Marmalade parsed correctly as Markdown. 

I'm going to try something different, that I hope will be more powerful. Namely, making code blocks a first-class citizen in the environment.

##Minimal Macros

Our bare minimum task: specify certain code blocks as files, create anchor macros to put in the code blocks, and specify certain other code blocks as the source for those macros. Anchor macros look ` #|(like this)|# ` in Clojure; we use different trigrams for different programming environments. It would be easy to get weird with it if trigrams don't suffice.

File and source macros are header macros, and will look something like this: ```` >## file:src/foo/core.clj ```` and ````>## source:like this```. I'm split on allowing whitespace in macros, but don't see the harm. I may want to normalize the whitespace if I do.

The rule is, currently, one block per file, and one block per source. I will relax that for files, and not for sources, in exactly one way: if a block says ```` >## file:src/somefile.clj \n ```clojure  ````, and the next block says ```` ```clojure~ ```` (note the `~`), that block is also part of the same file.

Will I allow the interleaving of multiple languages? Maybe. I can see the use case but don't want to make this harder to reason about.

I can think of some ways to expand this that might be nice. Some anchors might resolve to a single line or symbol and it could be convenient to embed several of those in a code block. If so, they will have a distinct "container" format. A code block must have either free code or any number of containers, but not both. I won't add this unless I'm feeling pain.

There are both developer and user justifications for this. As a developer, it happens that it's easy to make decisions about the code body from the code header. It's already packed up into a nice tree.

This conceptual clarity will help the user also. When we develop Athena further, it will be easy to turn our file macros into links into the tangle that provide the created file, and mark up the file name attractively as a header for the code block. Similarly, the source names can be converted into legible headers, and the anchors turned into links into the source code block.

Also, code is the land and Markdown is the ocean. Treating the blocks as mostly disconnected and atomic units will aid clarity, encourage the use of macros, and make the resulting weave easier to read.

Most importantly, this make writing Marmion difficult, not fiendishly difficult with numerous failure modes. This reduces merge conflicts to one case: edits to a macro source, in the tangle, where that macro is expanded in more than one place. This kind of thing is uncommon.

Marmalade macros are meta-macros. If you want to do fancy macro stuff, use a language that supports it. A major justification for writing Marmalade is polyglot codebases, so we can assemble working systems using the best tools for the job. That's what we do anyway, and the Marmalade toolchain systematizes it.

##Marmalade Proper

By adding header macros, we have officially made a dialect of GFM. Marmalade files do not parse as Markdown, causing me to take a day to write the first true Athena. She simply takes Marmalade code, strips the header macros, and emits compatible Markdown. Eventually, she can do a great deal more than that. But the circuit is complete; we may make breaking changes to our Markdown documents, generate the weave, and have readable Markdown which GitHub helpfully autogens into HTML.

Also, I just had my first conflicted edit! Because there's a weave, I edited the weave, not the tangle. This is going to be hard to fix down the line. I can add a function to Athena that generates special Markdown indicating that a weave is Woken and not Source, which would be useful during edit-intensive stages. Better yet, let Marmion back-propagate from both the weave and tangle. That's a tough boundary because the weave can become arbitrarily complex. A bridge too far. 

This combines well with Git: a Develop branch could have Marmalade tuned one way, and pushing to production would mean producing a clean weave. Something for later.

But this is the macro folder. Back to defining the macro behavior and adding it to Arachne, now that we can skillfully strip incompatible markup from our Marmalade source.

After that it's major-league refactor time here in the /source directory, as we pull all the Clojure in and clean up for an initial publication. I want this thing out there once the first two tools are added, since some smart developer who really knows git could toss Marmion off in a weekend.

##Expanding Macros

In general, an anchor macro should be on its own line. Arachne does not enforce this. By the nature of a code block, as we define it, there is a newline at the end, but not at the beginning. This means expanding an anchor macro from a one-line source block will add one newline, which may not be what you want if you're using Python and are trying to specify a variable name or the like.

Container macros will answer this, which is why I intend to eventually add them. I frown on weird stuff like redefining arbirary variables at a higher level; I anticipate legitimate use cases will come up and justify adding this wrinkle.

The parser-driven nature of Marmalade should make these kinds of changes much less painful.

Marmion will treat lines with anchor macros on them as boundaries, so in the edge case where there is code, then an anchor macro, and tangle edit touches the code before the anchor, Marmion will behave as though the macro was edited. This could cause weird things like the literal code getting back propagated into the anchor source.

That sounds like a bug in the making, actually. One may hope I grep for "marmion" before actually starting that codebase.

It won't affect Arachne, or Marmalade itself, which will be written in good style. Good Marmalade style dictates anchors which expand to code blocks get their own line.

##Header Macros

Header macros begin like this, on a newline: ` ># `. Any number of hashes is allowed. This corresponds to a block header, which parses well enough as pure Markdown. The code block must be directly below the header, exactly one newline is required and no more are allowed.

A code block with a header macro is called a tangle block.

I will start with three header macro types, two of which are necessary. The `file:` header macros specify a tangle block as a file container. The string following the command must be a relative path off the directory that will eventually be `tangle/` or whatever destination is chosen. Arachne is not aware of the `source/` directory's structure, though naturally, Athena is.

The `source:` command specifies that a tangle block is the source for the anchor macro. The parser will consume all whitespace between `source:` and the anchor, but will attach all whitespace after onto the anchor. I will probably normalize this before release because whitespace bugs suck, but I'm enamored of the ability to use actual, multiple words in anchors. This is *literate* programming, after all.

The `clone:` command is actually an Athena macro, and will be added later, as it's not on the critical path. `clone:` will take an anchor, and must be followed by an empty code block, containing exactly one newline. In this instance, whitespace is allowed on the newline, but is bad style. It will fill the code block, in the weave, with the `source:` of the anchor. Note that this is a **code** block, not featured in the tangle. 

This lets us document code in multiple places, and Athena will be able to link those code blocks back to the canonical `:source` definition for additional context.

As mentioned before, a `~` after the language tag on a header line means that the following code block is part of the `file:` structure that is being built up. Using it in the wrong place is an error and will halt the program.

Not implemented, but when it is, we will start with the restriction that one may build up only one `file:` structure at a time and must stick to a consistent language. Anything else would be harder to code and potentially confuse the user.
