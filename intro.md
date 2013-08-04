#Marmion

[Literate Programming](http://www-cs-faculty.stanford.edu/~uno/lp.html) is one of those paradigms whose fate is continual reinvention. I've been noticing that my software projects start as Markdown. It stands to reason that they should end up as Markdown as well.

[Git Flavored Markdown](https://help.github.com/articles/github-flavored-markdown), in particular, is crying out for a literate, multi-model programming system. The mechanism of named fenced code blocks lets one put multiple languages in a single file, and they will already be syntax highlighted according to the named language. 

As literate programming is for the ages, we shall call our system [Marmion](README.md). The weaver shall be known as [Athena](athena.md); the tangler, [Arachne]().

If at all possible, we don't want to touch GFM itself. Therefore, here are some principles:

* Code in fenced code blocks is extracted, macro-expanded, and executed in whatever ways are appropriate.

* Macros must employ patterns not used in a given language; therefore, we must be able to define those patterns.

* All configuration happens in special code blocks, called ```` ```config ````:

```clojure

{ :name "A config file",
  :format :edn
  :magic-number 42 } ;this is actually tagged ```clojure
```

* Code in regular unfenced code blocks is not included in the weave. Nor are fenced code blocks that aren't reached from the top macro. The code above, for example, *will not* be in the finished weave, because it is exemplary.

* All text ends up in the tangle, which is an HTML file. No other tangle format is contemplated. 

* If standardized, the tangle format will not be specified, only the markup format and the requirements for the subsequent weave. HTML is a moving target, as is visual display in general. 

* The Markdown may be extended, but only in the same way as any other code: by specifying a macro template and expanding it from provided code. It is the macro-expanded Markdown which is tangled and woven.

* Corollary: the Markdown is macro expanded before anything in a code block.  

* Corollary: the Markdown macro will be standard. There should be no reason to include it. Because Clojure is the implementation language, and has a defined reader macro syntax, this is already true of Clojure(Script).

* The weaver should visit all internal links in search of code. Some tag in HTML form should be provided so that fully-marked-up links, so tagged, will also be followed in search of exterior code. 

* If exterior code is requested, it is added to the source as a fenced code block. The tangle will preserve the link directly above the code block. Some sensible effort will be made to infer the code format from the file extension. This is to be done before macro expansion, so that if there are macros in the exterior code, they will be expanded.

* We should maintain a set of canonical macro patterns for languages, to encourage mutual compatibility in source and tangled code.

* No mechanism for transclusion on the file level will be provided. The file structure of the Markdown is the file structure of the tangle. Working around this using the tagged-link method will leave a broken link in your tangle.

This is the sort of project that we can tackle in stages. The most important part is the weaver, because we have a fine tangler in the form of [Jekyll](http://jekyllrb.com/). 

This is a job for [Clojure](http://clojure.org). The weaver and perhaps the tangler will be Clojurescript compatible in the narrow sense, but useless unless Instaparse is ported, which seems unlikely, though you never know. 

Clojure is chosen for a few reasons. [EDN](https://github.com/edn-format/edn), for one, which will be the format of any ```` ```config ```` code block. Also because of [Instaparse](https://github.com/Engelberg/instaparse), for which the usual regular-expression based markup approach is a strict subset of capabilities. It has the best story I'm aware of for setting regular expressions declaratively in a data format, which is exactly how we will provide macros. 

To be clear, this will let us syntax highlight a provided macro in a distinctive way, and put things like the colors to use right in the markdown. This is only useful with a completed weaver; Pygments will get the macros wrong but this is a minor stylistic matter which can be corrected by retangling with a better highlighter. 

Instaparse is my go-to choice for writing flexible parsers that are meant to be shared, so Clojure it is. I hope Instaparse catches on to the point where it becomes core, and hence worth maintaining separate `.clj` and `.cljs` versions. 

The first, and most important step, is writing [Athena](athena.md), the weaver. The weaver does the following: finds all the ```` ```config ```` code, parses it to configure itself, then goes after the code blocks, and uses the macros and config information to construct the weave. Finally, it calls the trigger file, which must contain everything needed to build the weave into an executable, or whatever the final product is.

The tangler, [Arachne](), should be a [fork of Jekyll](https://github.com/mnemnion/jekyll), with a low surface area of interaction. What I mean by this is that merges between the bases should avoid touching one another's files wherever possible. The only changes I contemplate personally is to plug-replace the syntax highlighter, for several reasons. 

Pygments requires one to write actual code to markup a new format. This is distasteful. Also, we need to markup the macros, which we won't know until we weave the code. Furthermore, a static syntax highlighter should be based on a powerful parser, not a regular engine janked up with extra Python. For pity's sake. 

If Marmion becomes popular, someone might want to write advanced capabilities: putting compatible code in a REPL, for example, or linking to one from the code, or linking to the line number in a public Github repository generated by the weaver. The last is particularly powerful. All of this will assuredly be easier with a parser-backed tangler. 

I'm clearly pulling a Knuth here: recursing through the Big Project until I hit something atomic and critical to further progress. Arc leads to GGG, which will benefit greatly from a literate style, which leads to Marmion. Marmion built, writing GGG in an understandable way becomes possible. 

I think I've painted myself into a corner, as I can't think of anything offhand which I need to write in order to write Marmion. 

Time to generate more Markdown!
