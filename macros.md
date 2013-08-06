#Macros.

The macro system needs to be flexible but not ridiculous. If we need non trivial code rewriting, we will choose a language which has it. Many, even most languages structure their code according to a convention which isn't necessarily that of the application logic. This is ultimately an imposition of the file structure, and cannot be helped.

This is the powerful case for literate programming: we may structure our thougthts into form. Programming usually travels up and down the ladder of abstraction: When at the top level, we want to be placidly unconcerned with the formatting of our program's source code. 

Our macros are simple rewrite macros. The inner contents can get somewhat complex, but conceptually there are three kinds: anchors, containers, and chains. Anchors provide a single point in the code base into which one may substitute. Containers provide two such points, and chains provides as many as one may care to define. 
