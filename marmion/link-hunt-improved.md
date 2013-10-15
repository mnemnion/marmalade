(* A Link Hunting Mini Grammar for Arachne *)

prose = categories+ 

<categories> = (stuff | link) / punct ;

<stuff> = #'[^\[\]()]+' ;

link = "[" #'[^\]]+' "]" WS* "(" directory? local-file ")" ;

directory = (#"[^):/]+" "/")+;

local-file = #"[^):/]+" ;

punct = #"[\[\]()]" ;

WS = #'[ ]+' ;