# super simple makefile
# call it using 'make NAME=name_of_code_file_without_extension'
# (assumes a .java extension)
NAME = "Main"
SRC = "src"
OUT = "out"

all:
	@echo "Compiling..."
	[ -d $(OUT) ] || mkdir $(OUT)
	javac -cp vecmath.jar $(SRC)/*.java -d $(OUT)
	#javac -cp src/vecmath.jar $(SRC) -d $(OUT) $(SRC)/*.java

run: all
	@echo "Running..."
	java -cp "vecmath.jar:./out" $(NAME)
	#java -cp "vecmath.jar:." $(OUT) $(NAME)

clean:
	rm -rf $(OUT)/*.class
