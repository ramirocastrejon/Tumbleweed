#!/usr/bin/env sh

javac -Xlint:unchecked --module-path ${PATH_TO_FX} --add-modules=javafx.controls,javafx.base,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web -cp lib/richtextfx-fat-0.10.2.jar -d out $(find src -name "*.java")

cp $(find src -name "*.fxml") out/editor
cp $(find src -name "*.css") out/editor
