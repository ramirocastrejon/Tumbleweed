package editor;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;

public class ConsoleOutput extends OutputStream {
    private javafx.scene.control.TextArea output;



    public ConsoleOutput(javafx.scene.control.TextArea consoleArea) {
        output=consoleArea;
    }

    @Override
    public void write(int i) throws IOException {
        output.appendText(String.valueOf((char)i));

    }
}
