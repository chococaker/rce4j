# rce4j

If you're reading this, then you should probably leave. This project is a piece of junk created
years ago when I was a Minecraft plugin developer who thought he knew how backdoors worked. I
believed that opening an HTTP listening port and receiving various commands through the port would
be simple enough to implement, and held off doing that until he finished most of the other
functions of a backdoor.

I had actually left all of these "other functions" UNTESTED, as I thought I would be able to test
them once the listening port was implemented. After opening the port on localhost, there were so
many bugs that I left most of the features untested, deprecating and deleting them left and right.
My justification was to "remove bloat" but honestly, the entire project is just bloat anyway.

And now, I'm releasing the project. Not as a usable framework for Java backdoor development, but as
a symbol of what happens when a project created without being planned out beforehand. If you are
insane enough to attempt to use the code in this project, please note that it is likely very
unstable and will not work.

However, to help you on your highly misguided journey (if you actually are trying to use some code
in here), I've stuck a list down below of all the features that I *probably* tested-- I have at
least a vague recollection of debugging those particular features.

- netty-server module (works on localhost only, so it's mostly useless)
- FileSystemModule (GET request only)
- EchoModule
- InfoModule
- org.rce4j.util.*
- NetUtil

### Disclaimer
If you use any code in this project, it is at your own risk. I highly do not recommend it. This
repository is posted more as a joke and a tribute to my wasted hours on poorly planned out code,
and is certainly not what is considered "production-ready code".
