package editor;

import java.io.*;

import javafx.stage.*;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import javafx.geometry.*;

public class AlertDialog {

    public static void display(String title, String message) {
        Stage window = new Stage();

        // Block input events while dialog is present.
        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);

        Button closeBtn = new Button("Ok");
        closeBtn.setOnAction(e -> {
            window.close();
        });

        VBox layout = new VBox(15);
        layout.getChildren().addAll(label, closeBtn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);

        // Display window.
        window.showAndWait();
    }
}
