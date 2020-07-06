#!/bin/bash
cd ./build/intermediates/classes/debug/
javac ../../../../src/main/java/TreeBuilder.java -d ./
java TreeBuilder
