#Marmalade

Marmalade is Marmion's Markdown dialect. For now, it's a wrapper over GFM, which is used to produce the weave proper. 

Athena goes from Marmalade to GFM, and eventually, conceivably, to pure HTML or other targets. Arachne parses Marmalade to produce the tangle. Marmion unravels changes to the tangle or weave back into the source.

Marmalade may extend GFM in various ways, the core function being macros. The macro form for the actual Markdown is `` `<@macro@>` ``, one of the nice things about Markdown being that one may literally quote a macro with double backticks and it will not be evaluated. 