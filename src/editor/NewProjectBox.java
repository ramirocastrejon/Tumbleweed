package editor;

import java.io.*;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.geometry.*;


public class NewProjectBox {

    static String projectName;

    public static String display(String title) {
        Stage window = new Stage();

        // block input events while AlertBox is present
        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText("Project Name");

        TextField projectNameField = new TextField();

        Button closeBtn = new Button("Create");
        closeBtn.setOnAction(e -> {
            projectName = projectNameField.getText();
            window.close();
        });

        VBox layout = new VBox(15);
        layout.getChildren().addAll(label, projectNameField, closeBtn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);

        // display window
        window.showAndWait();

        return projectName;
    }
}
