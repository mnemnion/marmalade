#Extending Instaparse

Instaparse is excellent. The more I use it the better it seems to get. That's me getting past the learning curve, clearly, and it shows that this is a real power tool. 

I wrote a rough draft of one of the commands, `insta/viz`, to put in a key function from the ANTLERverse. I'm working on one of these proposals for my own purposes: it will be somewhat hacky, since I'm in a hurry to write the good bits on top of it.

`(insta/re-parse parser tree)` takes the tree of your choice, flattens it into a single string, and reparses it with the provided parser. Which parser doesn't matter, except it should auto-detect format and provide accordingly unless overridden by keyword. Used with `insta/transform` this is a potent brew, and directly enables something I'll get to in a moment. I'm writing a re-parse function already.

`(insta/reader "grammar = 'string' ")` creates a reader. It works like `take` on a given string, returning the next form matching a discrete piece of literal data. If tokens are hidden, they are skipped, if the matched rule is hidden, the tokens are aggregated into a vector. A given reader maintains its state, and may be reset using the form `(insta/read foo-reader :reset)`.

`re-parse` enables progressive refinement, wherein a first rule matches a broad string and a second rule refines it. This is great for a text file containing several kinds of encoding with regular or literal token boundaries: we split on the boundaries, and then run the second set of rules on what we find.

This could be made explicit, by allowing multiple rule definitions in Instaparse. This would require more of the user, however, since they would have to understand that the second rule runs after a complete parse. A complete parse means different things in different contexts, if `reader` is implemented. Algorithmically this is perfectly straightforward, and I would argue that progressive refinement is easy to understand. What we do is `parse` or `read` the string, then `transform` using the `re-parse` rule on the `:rule-keyword` of the resulting tree. 