#An Interlude

Before we proceed with migraine (indeed my head hurts slightly, and I will eat before long), let's have another digression. I am nothing if not discursive.



#An Interlude

Before we proceed with migraine (indeed my head hurts slightly, and I will eat before long), let's have another digression. I am nothing if not discursive.

Marmion is intended as a production tool. To really get there, certain feedback loops are non optional. Marmion is a means to an end, but it should be made into an excellent means. We shouldn't want to discard Marmion until we have our own host environment. That may confidently be expected to take years. 

So Marmion is Athena, which weaves, and Arachne, which tangles. What, then, is Marmion itself? A mere aggregate, a chimera with the face of a virgin and the body of a spider? Marmion is the package, true, but Marmion per se, the command `marmion`, will perform the critical act of unraveling.

Unraveling doesn't fit into Donald Knuth's workflow. When one is publishing ones code as hardbound books, clearly, there is no time for debugging, and any errors found may be hand curated back into the source. 

This is not good enough for our purposes. The tangle will be an excellent guide to the code. Indeed, one may click on the tangle at any point containing code and be directed to the weave. It is to be expected that a fair amount of work may be done within the weave itself, and it would be a right pain to put that work back. What to do? 

Unravel, clearly. We take the weave, as generated from the source, and compare it to the updates in the weave, and use `diff` to put the various fixes back where they belong. There is probably some way to piggyback on `git` to do this directly.

The tangle is not the source, as we know, but it does contain the unexpanded macros found within code blocks, and regularly formatted HTML containing links which have the resulting line numbers within them. This is an achievable goal. 

Don't do refactoring from the weave. Refactor from the source, where it is enjoyable and clear. If we stay away from crossing logical boundaries with our edits, and most things which qualify as bug fixes wouldn't, we're in good shape. 

A sensible working environment will take a more durable, less quirky approach to aggregating data including what we now call source code. In the meantime, well, we don't have Marmion either. But we do have zeus, and that's more than we had two days ago.  

Another useful addition, which I'm sticking here because I lack another place to put it, would be support for [ASCIIMathML](http://www1.chapman.edu/~jipsen/mathml/asciimath.html) in the Markdown. Generating readable mathematics to go along with the code will prove handy someday I'm sure.

The syntax would be `` `$ASCIIMathML$ ``, inspired by TeX. The backtick is not used in ASCIIMathML markup; if you want a literal token with dollar signs on both sides, you must use double backticks to enclose it, or put spaces between the backtick and the dollar signs. 

This is an easy add when Arachne gets written. 
