#An Interlude

Before we proceed with migraine (indeed my head hurts slightly, and I will eat before long), let's have another digression. I am nothing if not discursive.

Marmion is intended as a production tool. To really get there, certain feedback loops are non optional. Marmion is a means to an end, but it should be made into an excellent means. We shouldn't want to discard Marmion until we have our own host environment. That may confidently be expected to take years. 

So Marmion is Athena, which weaves, and Arachne, which tangles. What, then, is Marmion itself? A mere aggregate, a chimera with the face of a virgin and the body of a spider? Marmion is the package, true, but Marmion per se, the command `marmion`, will perform the critical act of unraveling.

Unraveling doesn't fit into Donald Knuth's workflow. When one is publishing ones code as hardbound books, clearly, there is no time for debugging, and any errors found may be hand curated back into the source. 

This is not good enough for our purposes. The weave will be an excellent guide to the code. Indeed, one may click on the weave at any point containing code and be directed to the tangle. It is to be expected that a fair amount of work may be done within the tangle itself, and it would be a right pain to put that work back. What to do? 

Unravel, clearly. We take the tangle, as generated from the source, and compare it to the updates, using `diff` to put the various fixes back where they belong. There is probably some way to piggyback on `git` to do this directly.

Don't do refactoring from the tangle. Refactor from the source, where it is enjoyable and clear. If we stay away from crossing logical boundaries with our edits, and most things which qualify as bug fixes wouldn't, we're in good shape. The most important rule is not to modify the contents of a macro, because Marmion can't decide for you if the fix should propagate to other uses of the macro or not. (This only applies to a macro expanded in more than one place; many common macros are just placeholders for code and can be safely modified).

A sensible working environment will take a more durable, less quirky approach to aggregating data including what we now call source code. In the meantime, well, we don't have Marmion either. But we do have zeus, and that's more than we had two days ago.  

Another useful addition, which I'm sticking here because I lack another place to put it, would be support for [ASCIIMathML](http://www1.chapman.edu/~jipsen/mathml/asciimath.html) in the Markdown. Generating readable mathematics to go along with the code will prove handy someday I'm sure.

The syntax would be `` `$ASCIIMathML$` ``, inspired by TeX. The backtick is not used in ASCIIMathML markup; if you want a literal token with dollar signs on both sides, you must use double backticks to enclose it, or put spaces between the backtick and the dollar signs. 

This is an easy add when Athena gets written. 

##A twist

Your humble narrator is only human, and made a very basic mistake. A weaver creates the documentation, and a tangler the source code. All traces of this error are being erased from history; author's privilege.

Donald Knuth is a writer of books, that are incidentally programs. The books will inspire for generations after the source is run to any practical end, indeed the core TeX engine is largely superceded, and MMX is run only to explore Professor Knuth's work. Of course the weave creates the documentation. I feel quite foolish. Migraine indeed.

Well, we have a way out. I heard an ancient version of the myth in Crete, from an old man whose name was, well, they should have called him the bridge builder. But they didn't. He told me that when Athena broke loose from Zeus's forehead, a fragment of bone from his skull somehow ended up impregnating a human woman. He was actually quite graphic about the details, but in any case, thus was Arachne born. Much is explained: why Arachne was a better weaver in human eyes than Athena (she was born from the same thought), why Athena was so vengeful (she was a toddler as Olympians go), and why Arachne was ultimately forgiven and given domain over all spiders and things that crawl. 

Arachne is technically a spider, crawling a hypertext document, collating data of utility, and transforming it into something useful. Athena is a weaver, taking one hypertextual context and spinning it into another. The center holds.

Thus, Migraine will birth Athena, and in doing so, birth Arachne. The technical term is a fork. They do address the same input in complementary ways, so this is a sensible development. Stay tuned.
