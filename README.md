# Tumbleweed

## Project Requirements
1. ~~September 12 - Create/Close/Save/Open Project~~
2. September 26 - Create/Save/Open/Edit/Remove Files
3. October 10 - Compile project
4. October 10 - Execute project
5. October 10 - Provide real-time statistics about the number of keywords in the project
6. September 26 - Use a blue color for "while", "if", "else", "for"
7. September 26 - Use a  red color for arithmetic and Boolean operators 
8. September 26 (assuming) - Use green for strings

## Core Parts
* Editor (center) (6,7,8) -- Display/Edit text, color keywords, arithmetic and boolean operators, and strings
* Tree View (left) -- Show files/folders in current project (if no project default to user's HOME directory)
* Menubar (top) (1,2,3,4) -- Various operations 
* Status Bar (bottom) (5) -- Display project stats, which file the user is editing, how many lines the file has etc.
* Project View (right) -- Display project attributes, Name of project, Project Main Class, Project Classes, Libraries Used/Included

## Member Responsibilites
1. Editor -- Daniel
2. Tree View -- Ramiro
3. Menubar -- Jordan
4. Statusbar -- Kevin
5. Project View -- Rodger

## Compile and Run
### OSX/Linux
#### Shell
compile: 
```
javac --module-path $JAVAFX_HOME --add-modules=javafx.controls,javafx.base,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web -d out $(find src -name "*.java")

cp $(find src -name "*.fxml") out/editor
cp $(find src -name "*.css") out/editor
```
run:
```
java --module-path $JAVAFX_HOME --add-modules=javafx.controls,javafx.base,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web -cp out editor.Main`
```
Or simply use the compile.sh and run.sh included in the project
#### IntelliJ
install git: `sudo apt-get install git`

install the jdk: `sudo apt-get install openjdk11-jdk`

[Follow instructions for shell without JDK(JBR)](https://www.itzgeek.com/how-tos/linux/ubuntu-how-tos/how-to-install-intellij-idea-on-ubuntu-18-04-linux-mint-19.html)

Create Project from Version Control using https://www.github.com/jmhayes3/Tumbleweed

Say yes to all options

[Follow these instructions for linux from IDE non-modular step 3](https://openjfx.io/openjfx-docs/#IDE-Intellij)


### Windows

#### IntelliJ

[Download git for Windows](https://git-scm.com/download/win)

[Download Intellij](https://www.jetbrains.com/idea/)

Create Project from Version Control (also called checkout from version control) using https://www.github.com/jmhayes3/Tumbleweed

Say yes to all options

[Follow Step 3](https://openjfx.io/openjfx-docs/#IDE-Intellij) (Should be in Program Files/Java/jdk-your-version/lib/javafx-mx.jar)

Press the hammer icon (build) -> go to Run in the menubar -> select Run -> choose Main -> You should be able to run the application now.

You should get a runtime error, comment out lines 44 and 45 of Controller.java then re-run the application everything should work.

If you cannot run the application then follow Step 4 as well from  openjfx website.
