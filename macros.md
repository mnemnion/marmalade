#Macros.

The macro system needs to be flexible but not ridiculous. If we need non trivial code rewriting, we will choose a language which has it. Many, even most languages structure their code according to a convention which isn't necessarily that of the application logic. This is ultimately an imposition of the file structure, and cannot be helped.

This is the powerful case for literate programming: we may structure our thougthts into form. Programming usually travels up and down the ladder of abstraction: When at the top level, we want to be placidly unconcerned with the formatting of our program's source code. 

Our macros are simple rewrite macros. The inner contents can get somewhat complex, but conceptually there are three kinds: anchors, containers, and chains. Anchors provide a single point in the code base into which one may substitute. Containers provide two such points, and chains provides as many as one may care to define. 

This is easy to illustrate. For Markdown, a simple enough macro would be `` `<@ `` to begin a macro and `` @>` `` to end it. In between would be the name of the macro, or metadata, or what have you. That would suffice for a simple anchor macro.

A container (which is a chain of arity two, but is the most useful chain and has its own name) would start with `` `<@ ``, have a front matter closed with `` @> `` (note the lack of a closing `` ` ``) and immediately follow this with `` <@ ``. Whitespace between is not allowed, the middle symbol is `` @><@ `` in full. This is followed by literal source code, which is closed with `` @>` ``. A chain has more than one `` @><@ `` before the `` @>` `` which closes.

To make macro definitions clearer, let's pretend this is a macro for Foolang, which is found in ```` ```foolang ```` code blocks. Markdown macros may be more complex, as an edge case. Somewhere in a code block, we've called a macro `` `<@foomac@>` ``.

In its own codeblock, we put, as the first line, an anchor: `` `<@foomac/source@>` ``, with a newline immediately after. The code follows: one code block per macro. If it is a container or a chain, ` @><@ ` marks the points where literal code is written around.

That's it, that's all the macro we're providing. The metadata will allow language specific macros, which must be literal (no regular expression macro boundaries please!), and which, to work correctly, should be syntax errors anywhere outside of literal strings within your target language, in opening, closing, and continuation forms.



