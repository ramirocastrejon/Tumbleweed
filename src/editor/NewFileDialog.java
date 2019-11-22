package editor;

import java.io.*;

import javafx.stage.*;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import javafx.geometry.*;

public class NewFileDialog {

    static String fileName;

    public static String display(String title) {
        Stage window = new Stage();

        // Block input events while dialog is present.
        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText("File Name");

        TextField fileNameField = new TextField();

        Button closeBtn = new Button("Create");
        closeBtn.setOnAction(e -> {
            fileName = fileNameField.getText();
            window.close();
        });

        VBox layout = new VBox(15);
        layout.getChildren().addAll(label, fileNameField, closeBtn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);

        // Display window.
        window.showAndWait();

        return fileName;
    }
}
