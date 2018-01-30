## Compiling and Running Java Demos

To make ("compile") an example, use the included makefile with 
the name of the java file passed as a variable. For example, to make hello.java:

```bash
	make NAME="hello"
```

Then, to run:

```bash
    java hello 
```

Or you can compile and run in one step:

```bash
    make run NAME="hello"
```

You can also clean temporary `.class` files:

```bash
    make clean
```