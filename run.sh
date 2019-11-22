#!/usr/bin/env sh

java --module-path ${PATH_TO_FX} --add-modules=javafx.controls,javafx.base,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web -cp out:lib/richtextfx-fat-0.10.2.jar editor.Main
