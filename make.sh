#!/bin/bash

SRC_DIR="src"
BIN_DIR="bin"

mkdir -p $BIN_DIR


javac -d $BIN_DIR "$SRC_DIR/*.java"

if [ $? -eq 0 ]; then
    echo "Compilation successful. Class files are in the '$BIN_DIR' directory."
else
    echo "Compilation failed. Please check the source files for errors."
fi