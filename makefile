SRC_DIR=src
BIN_DIR=bin

SOURCES=$(shell find $(SRC_DIR) -name "*.java")

all: compile

compile:
	mkdir -p $(BIN_DIR)
	javac -d $(BIN_DIR) $(SOURCES)

run: compile
	java -cp $(BIN_DIR) Main

clean:
	rm -rf $(BIN_DIR)/*
