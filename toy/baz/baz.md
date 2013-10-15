#Baz

This here is baz.

Foo links to baz. Baz lives in its own subdirectory.

We'll put more here eventually I reckon.

Like a [code block!](http://addme.com).

```clojure source: baz

{:foo :bar, :baz :bux}

```

Now let's reload, shall we?


Let's also add an ordinary code block. This won't be reached by our tangler.

```clojure

{:do :not :see :me}

```

It should be correctly set in the proper weave, once we start weaving one.
