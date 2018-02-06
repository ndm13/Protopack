# Protopack
This is essentially a quick way to get Java `URLConnection`s to work with
simple protocols that are either "shortcuts" or are fairly easy to implement
and have fallen out of the spec.  It's much more a personal project that I
expand on as I need support for some random thing than anything stable.

Protopack is not intended to replace something like Apache Commons Net.
Instead, it's much more focused on providing a way to connect to various
protocols using the existing `java.net.URL` structure and methods.

## Installation
As easy as I could make it:

`ProtoPack.install();`

Adds Protopack's packages to the System property key.  Different JVMs may
cache this value, so it's best to run this as early in your code as possible.

## Currently Implemented Protocols
### Shortcuts
These are protocols that are really just representations of text or aliases
for more verbose links, so they're pretty easy to implement.

 - `javascript`
 - `data`
 
 ### Simple Protocols
 These take more work, since they involve looking up a spec and comparing
 implementations.  My rule of thumb is that I probably won't implement it
 if Apache Commons Net provides a method that's either really easy to use
 or has insanely low overhead for several connections.
 
  - `gopher`
