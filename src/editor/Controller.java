package editor;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.application.Platform;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Controller implements Initializable {

    @FXML
    private TextArea textArea;

    private Stage stage;
    private final FileChooser fileChooser = new FileChooser();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser.setInitialDirectory(new File(System.getenv("PWD")));
        directoryChooser.setInitialDirectory(new File(System.getenv("PWD")));
    }

    public void init(Stage primaryStage) {
        this.stage = primaryStage;
    }

    @FXML
    public void exit() {
        System.exit(0);
    }

    @FXML
    public void newProject() {
        String projectDir = NewProjectBox.display("Create Project");

        try {
            if (projectDir != null) {
                System.out.println("Creating project " + projectDir + "...");

                new File(projectDir + "/libs").mkdirs(); // returns a boolean

                File mainClassFile = new File(projectDir + "/Main.java");

                // can setup some basic code here in the file i.e. public class Main ...
                FileOutputStream fs = new FileOutputStream(mainClassFile);
                fs.close();

                stage.setTitle(directoryChooser.toString() + projectDir);

                System.out.println("Project created successfully.");
            } else {
                throw new FileNotFoundException("File not found.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openProject() {
        try {
            directoryChooser.setTitle("Open Project");
            File dir = directoryChooser.showDialog(stage);
            if (dir != null) {
                stage.setTitle(dir.toString());
            } else {
                throw new FileNotFoundException("File not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveProject() {
        System.out.println("Saving project...");
    }

    @FXML
    public void newFile() {
        textArea.clear();
    }
}
